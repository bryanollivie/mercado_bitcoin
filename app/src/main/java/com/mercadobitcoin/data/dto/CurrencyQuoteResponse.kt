package com.mercadobitcoin.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyQuoteResponse(
    @SerialName("data") val data: Map<String, List<AssetDto>>,
    @SerialName("status") val status: StatusDto
)

@Serializable
data class AssetDto(
    @SerialName("currency_id") val currencyId: Int,
    @SerialName("currency_name") val currencyName: String,
    @SerialName("currency_symbol") val currencySymbol: String,
    @SerialName("exchange_id") val exchangeId: Int,
    @SerialName("market_pair") val marketPair: String,
    @SerialName("quote") val quote: QuoteDto? = null
)

@Serializable
data class QuoteDto(
    @SerialName("USD") val usd: UsdQuoteDto? = null
)

@Serializable
data class UsdQuoteDto(
    @SerialName("price") val price: Double,
    @SerialName("volume_24h") val volume24h: Double? = null
)