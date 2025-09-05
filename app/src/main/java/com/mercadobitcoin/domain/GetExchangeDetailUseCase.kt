package com.mercadobitcoin.domain

import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeDetailUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<Result<ExchangeDetail>> {
        return repository.getExchangeDetail(id)
    }
}