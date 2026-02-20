package com.mercadobitcoin.data.repository

import android.os.Build
import app.cash.turbine.test
import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.core.common.DispatcherProvider
import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.core.network.HttpErrorHandler
import com.mercadobitcoin.data.local.dao.ExchangeDao
import com.mercadobitcoin.data.remote.dto.ExchangeDetailDto
import com.mercadobitcoin.data.remote.dto.ExchangeDetailResponse
import com.mercadobitcoin.data.remote.dto.ExchangeDto
import com.mercadobitcoin.data.remote.dto.ExchangeListResponse
import com.mercadobitcoin.data.remote.dto.StatusDto
import com.mercadobitcoin.data.remote.repository.ExchangeRepositoryImpl
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private lateinit var dao: ExchangeDao
    private lateinit var httpErrorHandler: HttpErrorHandler
    private lateinit var repository: ExchangeRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher = testDispatcher
        override val main: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setup() {
        mockkStatic(Build.VERSION::class)
        api = mockk()
        dao = mockk()
        httpErrorHandler = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getExchanges returns success with mapped data`() = runTest {
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
        coEvery { dao.getAll() } returns emptyList()
        coEvery { dao.clearAndInsertAll(any()) } just runs

        repository = ExchangeRepositoryImpl(api, dao, testDispatcherProvider, httpErrorHandler)

        repository.getExchangesWithDetails(1).test {
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            val success = awaitItem()
            assertTrue(success is AppResult.Success)
            val exchanges = (success as AppResult.Success).data
            assertEquals(1, exchanges.size)
            assertEquals("1", exchanges[0].id)
            assertEquals("Binance", exchanges[0].name)
            assertEquals("https://logo.url", exchanges[0].logoUrl)

            awaitComplete()
        }

        coVerify { dao.clearAndInsertAll(any()) }
    }

    @Test
    fun `getExchanges handles API error with empty cache`() = runTest {
        val response = Response.error<Any>(
            429,
            ResponseBody.create("application/json".toMediaType(), "{}")
        )
        val httpException = HttpException(response)

        coEvery { api.getExchanges(any(), any(), any()) } throws httpException
        coEvery { dao.getAll() } returns emptyList()
        every { httpErrorHandler.fromCode(429) } returns "Server instability"

        repository = ExchangeRepositoryImpl(api, dao, testDispatcherProvider, httpErrorHandler)

        repository.getExchangesWithDetails(1).test {
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            val error = awaitItem()
            assertTrue(error is AppResult.Error)
            val message = (error as AppResult.Error).message
            assertEquals("Server instability", message)

            awaitComplete()
        }
    }

    @Test
    fun `getExchanges falls back to cache on API error`() = runTest {
        val response = Response.error<Any>(
            500,
            ResponseBody.create("application/json".toMediaType(), "{}")
        )
        val httpException = HttpException(response)

        val cachedEntities = listOf(
            com.mercadobitcoin.data.local.entity.ExchangeEntity(
                id = "1",
                name = "Cached Exchange",
                logoUrl = null,
                spotVolumeUsd = "1000.50",
                dateLaunched = null
            )
        )

        coEvery { api.getExchanges(any(), any(), any()) } throws httpException
        coEvery { dao.getAll() } returns cachedEntities

        repository = ExchangeRepositoryImpl(api, dao, testDispatcherProvider, httpErrorHandler)

        repository.getExchangesWithDetails(1).test {
            val loading = awaitItem()
            assertTrue(loading is AppResult.Loading)

            val success = awaitItem()
            assertTrue(success is AppResult.Success)
            val data = (success as AppResult.Success).data
            assertEquals(1, data.size)
            assertEquals("Cached Exchange", data[0].name)
            assertTrue(success.fromCache == true)

            awaitComplete()
        }
    }
}
