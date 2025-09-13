package com.mercadobitcoin.data.repository

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.dto.CurrencyDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
     fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>>
     fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyDto>>>
     fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>>
}