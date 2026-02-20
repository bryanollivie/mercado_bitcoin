package com.mercadobitcoin.ui

import androidx.lifecycle.SavedStateHandle
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.dto.CurrencyDto
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.domain.usecase.GetExchangeCurrenciesUseCase
import com.mercadobitcoin.domain.usecase.GetExchangeDetailUseCase
import com.mercadobitcoin.ui.features.exchangedetails.ExchangeDetailViewModel
import com.mercadobitcoin.util.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ExchangeDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getExchangeDetailUseCase: GetExchangeDetailUseCase
    private lateinit var getExchangeCurrenciesUseCase: GetExchangeCurrenciesUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ExchangeDetailViewModel

    @Before
    fun setup() {
        getExchangeDetailUseCase = mockk()
        getExchangeCurrenciesUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf("exchangeId" to "1"))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when detail loads successfully, ui state is updated`() = runTest {
        val detail = ExchangeDetail(
            id = "1",
            name = "Binance",
            logoUrl = "https://logo.url",
            description = "Exchange description"
        )
        val currencies = listOf(
            CurrencyDto(cryptoId = 1, priceUsd = 50000.0, symbol = "BTC", name = "Bitcoin")
        )

        every { getExchangeDetailUseCase("1") } returns flow {
            emit(AppResult.Success(detail))
        }
        coEvery { getExchangeCurrenciesUseCase("1") } returns flow {
            emit(AppResult.Success(currencies))
        }

        viewModel = ExchangeDetailViewModel(
            savedStateHandle,
            getExchangeDetailUseCase,
            getExchangeCurrenciesUseCase
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.exchangeDetail)
        assertEquals("Binance", state.exchangeDetail?.name)
        assertEquals(1, state.exchangeDetail?.currencies?.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `when detail fails, ui state has error`() = runTest {
        every { getExchangeDetailUseCase("1") } returns flow {
            emit(AppResult.Error("Network error"))
        }

        viewModel = ExchangeDetailViewModel(
            savedStateHandle,
            getExchangeDetailUseCase,
            getExchangeCurrenciesUseCase
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertNull(state.exchangeDetail)
    }

    @Test
    fun `when currencies fail, ui state has error but keeps detail`() = runTest {
        val detail = ExchangeDetail(
            id = "1",
            name = "Binance"
        )

        every { getExchangeDetailUseCase("1") } returns flow {
            emit(AppResult.Success(detail))
        }
        coEvery { getExchangeCurrenciesUseCase("1") } returns flow {
            emit(AppResult.Error("Currencies error"))
        }

        viewModel = ExchangeDetailViewModel(
            savedStateHandle,
            getExchangeDetailUseCase,
            getExchangeCurrenciesUseCase
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Currencies error", state.error)
        assertNotNull(state.exchangeDetail)
    }

    @Test
    fun `refresh triggers new data load`() = runTest {
        var callCount = 0
        val detail = ExchangeDetail(id = "1", name = "Binance")

        every { getExchangeDetailUseCase("1") } returns flow {
            callCount++
            emit(AppResult.Success(detail))
        }
        coEvery { getExchangeCurrenciesUseCase("1") } returns flow {
            emit(AppResult.Success(emptyList<CurrencyDto>()))
        }

        viewModel = ExchangeDetailViewModel(
            savedStateHandle,
            getExchangeDetailUseCase,
            getExchangeCurrenciesUseCase
        )
        advanceUntilIdle()
        assertEquals(1, callCount)

        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(2, callCount)
    }
}
