package com.mercadobitcoin.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    val status: StatusDto,
    val data: List<AssetsDto>
)

@Serializable
data class AssetsDto(
    @SerialName("wallet_address") val walletAddress: String,
    val balance: Double,
    val platform: PlatformDto,
    val currency: CurrencyDto
)

@Serializable
data class PlatformDto(
    @SerialName("crypto_id") val cryptoId: Int,
    val symbol: String,
    val name: String
)

@Serializable
data class CurrencyDto(
    @SerialName("crypto_id") val cryptoId: Int,
    @SerialName("price_usd") val priceUsd: Double,
    val symbol: String,
    val name: String
)
