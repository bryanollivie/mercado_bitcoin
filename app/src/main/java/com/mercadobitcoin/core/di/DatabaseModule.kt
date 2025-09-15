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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mercado_bitcoin.db"
        ).build()

    @Provides
    fun provideExchangeDao(db: AppDatabase): ExchangeDao = db.exchangeDao()
}
