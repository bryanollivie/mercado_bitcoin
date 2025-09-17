package com.mercadobitcoin.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mercadobitcoin.data.local.dao.ExchangeDao
import com.mercadobitcoin.data.local.entity.ExchangeEntity

@Database(entities = [ExchangeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeDao(): ExchangeDao
}
