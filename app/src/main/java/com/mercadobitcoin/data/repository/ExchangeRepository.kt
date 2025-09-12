package com.mercadobitcoin.data.repository

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.util.AppResult
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
     fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>>
     fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyQuote>>>
     fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>>
}