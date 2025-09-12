package com.mercadobitcoin.data.repository

import android.os.Build
import app.cash.turbine.test
import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.data.dto.ExchangeDetailDto
import com.mercadobitcoin.data.dto.ExchangeDetailResponse
import com.mercadobitcoin.data.dto.ExchangeDto
import com.mercadobitcoin.data.dto.ExchangeListResponse
import com.mercadobitcoin.data.dto.StatusDto
import com.mercadobitcoin.util.AppResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class ExchangeRepositoryImplTest {

    private lateinit var api: ApiService
    private lateinit var repository: ExchangeRepositoryImpl

    @Before
    fun setup() {
        mockkStatic(Build.VERSION::class)
        api = mockk()
        repository = ExchangeRepositoryImpl(api)

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getExchanges returns success with mapped data`() = runTest {
        // Given
        val exchangeDto = ExchangeDto(
            id = 1,
            name = "Binance",
            slug = "binance",
            isActive = 1
        )

        val detailDto = ExchangeDetailDto(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "https://logo.url",
            description = "Description",
            dateLaunched = "2017-07-14",
            urls = null,
            makerFee = 0.1,
            takerFee = 0.1,
            spotVolumeUsd = 1000000.0
        )

        val listResponse = ExchangeListResponse(
            data = listOf(exchangeDto),
            status = StatusDto("", 0, null)
        )

        val detailResponse = ExchangeDetailResponse(
            data = mapOf("1" to detailDto),
            status = StatusDto("", 0, null)
        )

        coEvery { api.getExchanges(any(), any(), any()) } returns listResponse
        coEvery { api.getExchangeInfo(any()) } returns detailResponse

        // When & Then
        repository.getExchangesWithDetails(1).test {
            // Loading
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            // Success
            val success = awaitItem()
            assertTrue(success is AppResult.Success)
            val exchanges = (success as AppResult.Success).data
            assertEquals(1, exchanges.size)
            assertEquals("1", exchanges[0].id)
            assertEquals("Binance", exchanges[0].name)
            assertEquals("https://logo.url", exchanges[0].logoUrl)

            awaitComplete()
        }
    }

    @Test
    fun `getExchanges handles API error correctly`() = runTest {

        val response = Response.error<Any>(
            429,
            ResponseBody.create("application/json".toMediaType(), "{}")
        )
        val httpException = HttpException(response)

        coEvery { api.getExchanges(any(), any(), any()) } throws httpException

        // When & Then
        repository.getExchangesWithDetails(1).test {
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            val error = awaitItem()
            assertTrue(error is AppResult.Error)
            val message = (error as AppResult.Error).message
            assertEquals("HTTP 429 Response.error()", message)

            awaitComplete()
        }
    }

}