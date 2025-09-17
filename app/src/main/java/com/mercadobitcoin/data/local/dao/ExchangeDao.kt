package com.mercadobitcoin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mercadobitcoin.data.local.entity.ExchangeEntity

@Dao
interface ExchangeDao {
    @Query("SELECT * FROM exchanges")
    suspend fun getAll(): List<ExchangeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchange: ExchangeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exchanges: List<ExchangeEntity>)

    @Query("DELETE FROM exchanges")
    suspend fun clearAll()
}
