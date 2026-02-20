package com.mercadobitcoin.core.di

import android.content.Context
import androidx.room.Room
import com.mercadobitcoin.core.database.AppDatabase
import com.mercadobitcoin.data.local.dao.ExchangeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modulo Hilt para configuracao do banco de dados Room.
 * Utiliza [fallbackToDestructiveMigration] pois a tabela de exchanges
 * e apenas cache — dados podem ser reconstruidos a partir da API.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Cria a instancia singleton do Room database. */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mercado_bitcoin.db"
        ).fallbackToDestructiveMigration().build()

    /** Expoe o DAO de exchanges a partir da instancia do database. */
    @Provides
    fun provideExchangeDao(db: AppDatabase): ExchangeDao = db.exchangeDao()
}
