package com.mercadobitcoin.data.remote.dto.mapper

import android.os.Build
import com.mercadobitcoin.data.remote.dto.AssetDto
import com.mercadobitcoin.data.remote.dto.ExchangeDetailDto
import com.mercadobitcoin.data.remote.dto.ExchangeDto
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun ExchangeDto.toDomainModel(detailDto: ExchangeDetailDto? = null): Exchange {
    return Exchange(
        id = id.toString(),
        name = name,
        logoUrl = detailDto?.logo,
        spotVolumeUsd = detailDto?.spotVolumeUsd?.let { BigDecimal(it) },
        dateLaunched = detailDto?.dateLaunched?.let { parseDate(it) }
    )
}

fun ExchangeDetailDto.toDomainModel(): ExchangeDetail {
    return ExchangeDetail(
        id = id.toString(),
        name = name,
        logoUrl = logo,
        description = description,
        websiteUrl = urls?.website?.firstOrNull(),
        makerFee = makerFee?.let { BigDecimal(it) },
        takerFee = takerFee?.let { BigDecimal(it) },
        dateLaunched = dateLaunched?.let { parseDate(it) }
    )
}

fun AssetDto.toCurrencyQuote(): CurrencyQuote? {
    val price = quote?.usd?.price ?: return null
    return CurrencyQuote(
        name = currencyName,
        priceUsd = BigDecimal(price)
    )
}

private fun parseDate(dateString: String): LocalDate? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(dateString.take(10), DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    } catch (e: Exception) {
        null
    }
}