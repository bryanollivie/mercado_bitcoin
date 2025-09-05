package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeCurrenciesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<Result<List<CurrencyQuote>>> {
        return repository.getExchangeCurrencies(id)
    }
}