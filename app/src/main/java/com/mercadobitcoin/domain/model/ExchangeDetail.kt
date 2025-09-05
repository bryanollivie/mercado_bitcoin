package com.mercadobitcoin.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class ExchangeDetail(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val description: String? = null,
    val websiteUrl: String? = null,
    val makerFee: BigDecimal? = null,
    val takerFee: BigDecimal? = null,
    val dateLaunched: LocalDate? = null,
    //val currencies: List<CurrencyQuote> = emptyList()
)