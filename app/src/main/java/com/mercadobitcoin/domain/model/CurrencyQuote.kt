package com.mercadobitcoin.domain.model

import java.math.BigDecimal

data class CurrencyQuote(
    val name: String,
    val priceUsd: BigDecimal
)