package com.mercadobitcoin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchanges")
data class ExchangeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String?,
    val spotVolumeUsd: String?,
    val dateLaunched: String?
)
