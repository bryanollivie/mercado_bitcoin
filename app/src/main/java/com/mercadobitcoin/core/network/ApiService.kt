package com.mercadobitcoin.core.network

import com.mercadobitcoin.data.dto.CurrencyQuoteResponse
import com.mercadobitcoin.data.dto.ExchangeDetailResponse
import com.mercadobitcoin.data.dto.ExchangeListResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "id"
    ): ExchangeListResponse

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: String
    ): ExchangeDetailResponse

    @GET("v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: String
    ): CurrencyQuoteResponse
}
/*
interface ApiService {

    */
/** 1) Lista básica de exchanges (ids + nomes) *//*

    @GET("v1/exchange/map")
    suspend fun getExchanges(@Query("limit") limit: Int = 50, @Query("start") start: Int = 1): Response<List<CmcExchangeMapItem>>

    */
/** 2) Info/detalhe da(s) exchange(s) — aceita CSV de ids *//*

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") idCsv: String
    ): Response<Map<String, CmcExchangeInfo>>

    */
/** 3) Pairs/quotes para extrair moedas + preço em USD *//*

    @GET("v1/exchange/market-pairs/latest")
    suspend fun getExchangeMarketPairs(
        @Query("id") id: Int,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD"
    ): Response<CmcMarketPairsData>
}

*/
/* ====== Wrappers/DTOs mínimos ====== *//*


@JsonClass(generateAdapter = true)
data class Response<T>(
    val status: CmcStatus,
    val data: T?
)

@JsonClass(generateAdapter = true)
data class CmcStatus(
    @Json(name = "error_code") val errorCode: Int,
    @Json(name = "error_message") val errorMessage: String?
)

@JsonClass(generateAdapter = true)
data class CmcExchangeMapItem(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class CmcExchangeInfo(
    val id: Int,
    val name: String,
    val description: String?,
    @Json(name = "date_launched") val dateLaunched: String?,
    val urls: CmcUrls?,                // para website
    val logo: String?,
    @Json(name = "maker_fee") val makerFee: Double?,
    @Json(name = "taker_fee") val takerFee: Double?,
    @Json(name = "spot_volume_usd") val spotVolumeUsd: Double?
)

@JsonClass(generateAdapter = true)
data class CmcUrls(val website: List<String>?)

@JsonClass(generateAdapter = true)
data class CmcMarketPairsData(
    @Json(name = "market_pairs") val marketPairs: List<CmcMarketPair> = emptyList()
)

@JsonClass(generateAdapter = true)
data class CmcMarketPair(
    @Json(name = "base_symbol") val baseSymbol: String?,                // nome da moeda
    @Json(name = "quote") val quote: Map<String, QuoteDetail>?          // para pegar USD.price
)

@JsonClass(generateAdapter = true)
data class QuoteDetail(val price: Double?)
*/
