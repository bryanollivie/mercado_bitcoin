/*
package com.mercadobitcoin.ui

import app.cash.turbine.test
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import com.mercadobitcoin.ui.features.exchanges.ExchangesUiState
import com.mercadobitcoin.ui.features.exchanges.ExchangesViewModel
import com.mercadobitcoin.util.AppResult
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class ExchangesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRu()
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getExchangesUseCase: GetExchangesUseCase
    private lateinit var viewModel: ExchangesViewModel

    @Before
    fun setup() {
        getExchangesUseCase = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `initial state is loading when viewModel is created`() = runTest {
        // Given
        coEvery { getExchangesUseCase(1) } returns flow {
            emit(AppResult.Loading)
        }

        // When
        viewModel = ExchangesViewModel(getExchangesUseCase)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
            assertTrue(state.exchanges.isEmpty())
            assertNull(state.error)
        }
    }

    @Test
    fun `when use case returns success, ui state is updated with exchanges`() = runTest {
        // Given
        val exchanges = listOf(
            Exchange("1", "Binance", null, BigDecimal.TEN, null),
            Exchange("2", "Coinbase", null, BigDecimal.ONE, null)
        )

        coEvery { getExchangesUseCase(1) } returns flow {
            emit(AppResult.Success(exchanges))
        }

        // When
        viewModel = ExchangesViewModel(getExchangesUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.exchanges.size)
        assertEquals("Binance", state.exchanges[0].name)
        assertNull(state.error)
    }

    @Test
    fun `when use case returns error, ui state contains error message`() = runTest {
        // Given
        coEvery { getExchangesUseCase(1) } returns flow {
            emit(AppResult.Error(Exception("API Error").message.toString()))
        }

        // When
        viewModel = ExchangesViewModel(getExchangesUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.exchanges.isEmpty())
        assertEquals("API Error", state.error)
    }

    @Test
    fun `refresh triggers new data load`() = runTest {
        // Given
        var callCount = 0
        coEvery { getExchangesUseCase(1) } returns flow {
            callCount++
            emit(AppResult.Success(emptyList<Exchange>()))
        }

        viewModel = ExchangesViewModel(getExchangesUseCase)
        advanceUntilIdle()
        assertEquals(1, callCount)

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        assertEquals(2, callCount)
        coVerify(exactly = 2) { getExchangesUseCase(1) }
    }

    @Test
    fun `state transitions correctly from loading to success to error`() = runTest {
        // Given
        val exchanges = listOf(Exchange("1", "Test", null, null, null))
        val stateFlow = mutableListOf<ExchangesUiState>()

        coEvery { getExchangesUseCase(1) } returnsMany listOf(
            flow {
                emit(AppResult.Loading)
                emit(AppResult.Success(exchanges))
            },
            flow {
                emit(AppResult.Loading)
                emit(AppResult.Error(Exception("Error").message.toString()))
            }
        )

        // When
        viewModel = ExchangesViewModel(getExchangesUseCase)

        // Collect states
        val job = launch {
            viewModel.uiState.collect { stateFlow.add(it) }
        }

        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        job.cancel()

        // Then
        assertTrue(stateFlow.any { it.isLoading })
        assertTrue(stateFlow.any { it.exchanges.isNotEmpty() })
        assertTrue(stateFlow.any { it.error != null })
    }
}*/
