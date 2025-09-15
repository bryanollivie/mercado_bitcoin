package com.mercadobitcoin.data.mapper

import android.os.Build
import com.mercadobitcoin.data.local.entity.ExchangeEntity
import com.mercadobitcoin.data.remote.dto.ExchangeDetailDto
import com.mercadobitcoin.data.remote.dto.ExchangeDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Mappers de DTO ↔ Domain ↔ Entity
 */
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

fun Exchange.toEntity() = ExchangeEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.toDouble(),
    dateLaunched = dateLaunched?.format(DateTimeFormatter.ISO_DATE) // formato fixo
)

fun ExchangeEntity.toDomain() = Exchange(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.toBigDecimal(),
    dateLaunched = dateLaunched?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
)

/**
 * Função auxiliar para parse seguro de datas
 */
private fun parseDate(dateString: String?): LocalDate? {
    if (dateString.isNullOrBlank()) return null
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(dateString.take(10), DateTimeFormatter.ISO_DATE)
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .parse(dateString.take(10))
                ?.toInstant()
                ?.atZone(ZoneId.systemDefault())
                ?.toLocalDate()
        }
    } catch (e: Exception) {
        null
    }
}

/*


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

fun Exchange.toEntity() = ExchangeEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.toDouble(),
    dateLaunched = dateLaunched?.toString()
)

fun ExchangeEntity.toDomain() = Exchange(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.toBigDecimal(),
    dateLaunched = dateLaunched?.let { LocalDate.parse(it) }
)


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
}*/
