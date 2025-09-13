package com.mercadobitcoin.domain.model

import java.math.BigDecimal

data class CurrencyQuote(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceUsd: BigDecimal
)
