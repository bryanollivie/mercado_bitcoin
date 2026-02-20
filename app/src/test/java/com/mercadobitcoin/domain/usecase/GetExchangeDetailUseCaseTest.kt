package com.mercadobitcoin.domain.usecase

import app.cash.turbine.test
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.ExchangeDetail
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetExchangeDetailUseCaseTest {

    private lateinit var repository: ExchangeRepository
    private lateinit var useCase: GetExchangeDetailUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetExchangeDetailUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when repository returns success, use case emits success with detail`() = runTest {
        val detail = ExchangeDetail(
            id = "1",
            name = "Binance",
            logoUrl = "https://logo.url",
            description = "A crypto exchange"
        )

        every { repository.getExchangeDetail("1") } returns flow {
            emit(AppResult.Success(detail))
        }

        useCase("1").test {
            val success = awaitItem()
            assertTrue(success is AppResult.Success)
            assertEquals("Binance", (success as AppResult.Success).data.name)
            awaitComplete()
        }

        verify(exactly = 1) { repository.getExchangeDetail("1") }
    }

    @Test
    fun `when repository returns error, use case emits error`() = runTest {
        every { repository.getExchangeDetail("999") } returns flow {
            emit(AppResult.Error("Not found"))
        }

        useCase("999").test {
            val error = awaitItem()
            assertTrue(error is AppResult.Error)
            assertEquals("Not found", (error as AppResult.Error).message)
            awaitComplete()
        }
    }
}
