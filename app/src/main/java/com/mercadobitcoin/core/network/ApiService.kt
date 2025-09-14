package com.mercadobitcoin.core.network

import com.mercadobitcoin.data.dto.AssetsResponse
import com.mercadobitcoin.data.dto.ExchangeDetailResponse
import com.mercadobitcoin.data.dto.ExchangeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 15,
        @Query("sort") sort: String = "id"
    ): ExchangeListResponse

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: String
    ): ExchangeDetailResponse

    @GET("v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: String
    ): AssetsResponse
}