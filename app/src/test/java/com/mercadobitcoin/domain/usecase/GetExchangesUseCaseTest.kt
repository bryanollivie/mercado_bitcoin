package com.mercadobitcoin.domain.usecase


import app.cash.turbine.test
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.core.common.AppResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

@ExperimentalCoroutinesApi
class GetExchangesUseCaseTest {

    private lateinit var repository: ExchangeRepository
    private lateinit var useCase: GetExchangesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetExchangesUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when repository returns success, use case emits loading then success`() = runTest {
        // Given
        val exchanges = listOf(
            Exchange(
                id = "1",
                name = "Binance",
                logoUrl = "https://example.com/binance.png",
                spotVolumeUsd = BigDecimal("1000000.00"),
                dateLaunched = LocalDate.of(2017, 7, 14)
            ),
            Exchange(
                id = "2",
                name = "Coinbase",
                logoUrl = "https://example.com/coinbase.png",
                spotVolumeUsd = BigDecimal("500000.00"),
                dateLaunched = LocalDate.of(2012, 6, 20)
            )
        )

        coEvery { repository.getExchangesWithDetails(1) } returns flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(exchanges))
        }

        // When & Then
        useCase(page = 1).test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            // Second emission should be Success with data
            val success = awaitItem()
            assertTrue(success is AppResult.Success)
            assertEquals(2, (success as AppResult.Success).data.size)
            assertEquals("Binance", success.data[0].name)

            awaitComplete()
        }

        // Verify repository was called
        coVerify(exactly = 1) { repository.getExchangesWithDetails(1) }
    }

    @Test
    fun `when repository returns error, use case emits loading then error`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { repository.getExchangesWithDetails(1) } returns flow {
            emit(AppResult.Loading)
            emit(AppResult.Error(exception.message.toString()))
        }

        // When & Then
        useCase(1).test {
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            val error = awaitItem()
            assertTrue(error is AppResult.Error)
            assertEquals("Network error", (error as AppResult.Error).message.toString())

            awaitComplete()
        }
    }

    @Test
    fun `when multiple pages requested, each calls repository correctly`() = runTest {
        // Given
        coEvery { repository.getExchangesWithDetails(any()) } returns flowOf(
            AppResult.Success(emptyList())
        )

        // When
        val pages = listOf(1, 2, 3)
        pages.forEach { page ->
            useCase(page).test {
                awaitItem()
                awaitComplete()
            }
        }

        // Then
        pages.forEach { page ->
            coVerify(exactly = 1) { repository.getExchangesWithDetails(page) }
        }
    }
}