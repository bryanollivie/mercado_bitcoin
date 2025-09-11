package com.mercadobitcoin.core.common

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatter {
    private val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    private val percentFormat = DecimalFormat("#,##0.00%", DecimalFormatSymbols(Locale.US))

    fun formatCurrency(value: BigDecimal?): String {
        return value?.let { "$ ${decimalFormat.format(it)}" } ?: "$ 0.00"
    }

    fun formatPercent(value: BigDecimal?): String {
        return value?.let { percentFormat.format(it.divide(BigDecimal(100))) } ?: "0.00%"
    }
}