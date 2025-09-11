package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.data.repository.ExchangeRepository
import com.mercadobitcoin.util.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeCurrenciesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<AppResult<List<CurrencyQuote>>> {
        return repository.getExchangeCurrencies(id)
        //return repository.getExchangeCurrencies(id)
    }
}