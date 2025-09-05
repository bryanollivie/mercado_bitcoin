package com.mercadobitcoin.domain

import com.mercadobitcoin.domain.model.Exchange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(page: Int = 1): Flow<Result<List<Exchange>>> {
        return repository.getExchanges(page)
    }
}