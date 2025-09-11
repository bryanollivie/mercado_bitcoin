package com.mercadobitcoin.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeDetailResponse(
    @SerialName("data") val data: Map<String, ExchangeDetailDto>,
    @SerialName("status") val status: StatusDto
)

@Serializable
data class ExchangeDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("logo") val logo: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("date_launched") val dateLaunched: String? = null,
    @SerialName("urls") val urls: UrlsDto? = null,
    @SerialName("maker_fee") val makerFee: Double? = null,
    @SerialName("taker_fee") val takerFee: Double? = null,
    @SerialName("spot_volume_usd") val spotVolumeUsd: Double? = null
)

@Serializable
data class UrlsDto(
    @SerialName("website") val website: List<String>? = null,
    @SerialName("twitter") val twitter: List<String>? = null,
    @SerialName("chat") val chat: List<String>? = null
)