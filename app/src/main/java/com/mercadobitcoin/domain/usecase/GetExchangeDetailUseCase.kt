package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.data.repository.ExchangeRepository
import com.mercadobitcoin.util.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeDetailUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<AppResult<ExchangeDetail>> {
        return repository.getExchangeDetail(id)
    }
}