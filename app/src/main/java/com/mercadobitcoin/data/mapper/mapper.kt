package com.mercadobitcoin.data.mapper

import android.os.Build
import com.mercadobitcoin.data.dto.ExchangeDetailDto
import com.mercadobitcoin.data.dto.ExchangeDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


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

private fun parseDate(dateString: String): LocalDate? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(dateString.take(10), DateTimeFormatter.ISO_DATE)
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString)?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        }
    } catch (e: Exception) {
        null
    }
}