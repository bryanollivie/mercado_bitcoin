package com.mercadobitcoin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ExchangeDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("is_active") val isActive: Int? = null
)

@Serializable
data class ExchangeListResponse(
    @SerialName("data") val data: List<ExchangeDto>,
    @SerialName("status") val status: StatusDto
)

@Serializable
data class StatusDto(
    @SerialName("timestamp") val timestamp: String,
    @SerialName("error_code") val errorCode: Int,
    @SerialName("error_message") val errorMessage: String? = null
)