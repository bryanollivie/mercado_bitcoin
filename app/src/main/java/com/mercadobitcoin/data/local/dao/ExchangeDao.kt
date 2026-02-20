package com.mercadobitcoin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mercadobitcoin.data.local.entity.ExchangeEntity

/**
 * DAO Room para acesso a tabela de exchanges em cache local.
 * Serve como fonte de dados offline para a estrategia cache-first do repositorio.
 */
@Dao
interface ExchangeDao {

    /** Retorna todas as exchanges salvas no cache local. */
    @Query("SELECT * FROM exchanges")
    suspend fun getAll(): List<ExchangeEntity>

    /** Insere uma unica exchange, substituindo em caso de conflito de PK. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchange: ExchangeEntity)

    /** Insere uma lista de exchanges, substituindo em caso de conflito de PK. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exchanges: List<ExchangeEntity>)

    /** Remove todas as exchanges do cache local. */
    @Query("DELETE FROM exchanges")
    suspend fun clearAll()

    /**
     * Substitui todo o cache de forma atomica.
     * Garante que o cache nunca fique vazio entre o clear e o insert,
     * mesmo em caso de crash ou cancelamento da coroutine.
     */
    @Transaction
    suspend fun clearAndInsertAll(exchanges: List<ExchangeEntity>) {
        clearAll()
        insertAll(exchanges)
    }
}
