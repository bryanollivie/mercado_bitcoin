package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.dto.CurrencyDto
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para buscar as criptomoedas negociadas em uma exchange.
 * Retorna a lista de moedas com preco em USD e simbolo.
 *
 * Chamado pelo [ExchangeDetailViewModel] encadeado apos o carregamento
 * do detalhe da exchange (via flatMapLatest).
 */
class GetExchangeCurrenciesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    /**
     * @param id identificador da exchange.
     * @return flow que emite Loading -> lista de moedas ou erro.
     */
    suspend operator fun invoke(id: String): Flow<AppResult<List<CurrencyDto>>> {
        return repository.getExchangeCurrencies(id)
    }
}
