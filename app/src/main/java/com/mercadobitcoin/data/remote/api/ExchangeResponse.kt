/*
package com.mercadobitcoin.data.remote.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CmcResponse<T>(
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
    val name: String,
    val slug: String?,
    @Json(name = "is_active") val isActive: Int?,
    @Json(name = "is_listed") val isListed: Int?,
    @Json(name = "is_redistributable") val isRedistributable: Int?,
    @Json(name = "first_historical_data") val firstHistoricalData: String?,
    @Json(name = "last_historical_data") val lastHistoricalData: String?
)*/
