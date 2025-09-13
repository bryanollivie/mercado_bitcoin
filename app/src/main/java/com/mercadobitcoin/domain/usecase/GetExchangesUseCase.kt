package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.data.repository.ExchangeRepository
import com.mercadobitcoin.core.common.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(page: Int = 1): Flow<AppResult<List<Exchange>>> {
        return repository.getExchangesWithDetails(page)
    }
}