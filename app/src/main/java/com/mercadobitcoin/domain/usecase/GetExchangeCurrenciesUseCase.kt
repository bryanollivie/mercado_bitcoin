package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.data.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.core.common.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeCurrenciesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<AppResult<List<CurrencyQuote>>> {
        return repository.getExchangeCurrencies(id)
    }
}