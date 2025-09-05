package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(page: Int = 1): Flow<Result<List<Exchange>>> {
        return repository.getExchanges(page)
    }
}