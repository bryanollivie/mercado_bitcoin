package com.mercadobitcoin.core.di

import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.domain.usecase.GetExchangeCurrenciesUseCase
import com.mercadobitcoin.domain.usecase.GetExchangeDetailUseCase
import com.mercadobitcoin.domain.usecase.GetExchangesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetExchangesUseCase(repository: ExchangeRepository): GetExchangesUseCase {
        return GetExchangesUseCase(repository)
    }

    @Provides
    fun provideGetExchangeDetailUseCase(repository: ExchangeRepository): GetExchangeDetailUseCase {
        return GetExchangeDetailUseCase(repository)
    }

    @Provides
    fun provideGetExchangeCurrenciesUseCase(repository: ExchangeRepository): GetExchangeCurrenciesUseCase {
        return GetExchangeCurrenciesUseCase(repository)
    }
}