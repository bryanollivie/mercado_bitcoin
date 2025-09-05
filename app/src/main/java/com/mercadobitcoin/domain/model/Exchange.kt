package com.mercadobitcoin.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Exchange(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val spotVolumeUsd: BigDecimal? = null,
    val dateLaunched: LocalDate? = null
)