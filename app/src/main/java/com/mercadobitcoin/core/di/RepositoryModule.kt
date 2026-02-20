package com.mercadobitcoin.core.di

import com.mercadobitcoin.core.common.AndroidResourceProvider
import com.mercadobitcoin.core.common.ResourceProvider
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.data.remote.repository.ExchangeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modulo Hilt que vincula interfaces as suas implementacoes concretas.
 * Usa [@Binds] (mais eficiente que @Provides) pois nao requer logica de criacao.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /** Vincula [ExchangeRepositoryImpl] como implementacao de [ExchangeRepository]. */
    @Binds
    @Singleton
    abstract fun bindExchangeRepository(
        exchangeRepositoryImpl: ExchangeRepositoryImpl
    ): ExchangeRepository

    /** Vincula [AndroidResourceProvider] como implementacao de [ResourceProvider]. */
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        androidResourceProvider: AndroidResourceProvider
    ): ResourceProvider
}
