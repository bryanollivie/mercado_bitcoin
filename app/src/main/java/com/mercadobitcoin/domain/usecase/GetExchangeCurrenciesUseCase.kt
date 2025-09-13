package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.dto.CurrencyDto
import com.mercadobitcoin.data.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeCurrenciesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(id: String): Flow<AppResult<List<CurrencyDto>>> {
        return repository.getExchangeCurrencies(id)
    }
}