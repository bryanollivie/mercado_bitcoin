package com.mercadobitcoin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CurrencyQuoteResponse(
    @SerialName("data") val data: List<AssetDto>,
    @SerialName("status") val status: StatusCurrencyDto
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
data class StatusCurrencyDto(
    val timestamp: String,
    @SerialName("error_code") val errorCode: Int,
    @SerialName("error_message") val errorMessage: String? = null,
    val elapsed: Int,
    @SerialName("credit_count") val creditCount: Int,
    val notice: String? = null
)

data class CurrencyQuote(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceUsd: BigDecimal
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