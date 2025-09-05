package com.mercadobitcoin.domain

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    suspend fun getExchanges(page: Int): Flow<Result<List<Exchange>>>
    suspend fun getExchangeDetail(id: String): Flow<Result<ExchangeDetail>>
    suspend fun getExchangeCurrencies(id: String): Flow<Result<List<CurrencyQuote>>>
}