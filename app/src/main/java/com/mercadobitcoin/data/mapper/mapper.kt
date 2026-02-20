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
 * Extension functions de mapeamento entre as tres camadas de modelo:
 *   DTO (API) <-> Domain (negocio) <-> Entity (Room)
 *
 * Cada camada tem seu proprio modelo para manter a separacao de responsabilidades:
 * - DTOs usam tipos serializaveis (Double, String) compatíveis com a API.
 * - Domain usa tipos ricos (BigDecimal, LocalDate) para precisao e semantica.
 * - Entities usam tipos compatíveis com SQLite (String) para persistencia.
 */

/**
 * Converte um DTO da listagem de exchanges para o modelo de domínio.
 * Opcionalmente enriquece com detalhes (logo, volume, data) se o [detailDto] for fornecido.
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

/**
 * Converte um DTO de detalhe da exchange para o modelo de domínio [ExchangeDetail].
 * Extrai o primeiro website disponivel da lista de URLs.
 */
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

/**
 * Converte Exchange (domínio) para ExchangeEntity (Room).
 * O volume e armazenado como String (via [toPlainString]) para preservar
 * a precisao do BigDecimal, evitando a perda de casas decimais do Double.
 */
fun Exchange.toEntity() = ExchangeEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.toPlainString(),
    dateLaunched = dateLaunched?.format(DateTimeFormatter.ISO_DATE)
)

/**
 * Converte ExchangeEntity (Room) de volta para Exchange (domínio).
 * Reconstroi BigDecimal e LocalDate a partir das representacoes em String.
 */
fun ExchangeEntity.toDomain() = Exchange(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd?.let { BigDecimal(it) },
    dateLaunched = dateLaunched?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
)

/**
 * Parse seguro de datas no formato "yyyy-MM-dd".
 * Trunca a string nos primeiros 10 caracteres para lidar com formatos
 * como "2017-07-14T00:00:00.000Z" retornados pela API.
 * Suporta tanto a API java.time (SDK >= 26) quanto SimpleDateFormat (SDK < 26).
 *
 * @return [LocalDate] ou null em caso de formato invalido.
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
