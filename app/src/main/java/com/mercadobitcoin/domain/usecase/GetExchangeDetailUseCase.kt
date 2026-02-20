package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para buscar os detalhes completos de uma exchange.
 * Retorna informacoes como logo, descricao, fees e website.
 *
 * Chamado pelo [ExchangeDetailViewModel] como primeiro passo
 * do carregamento da tela de detalhes, antes de buscar as moedas.
 */
class GetExchangeDetailUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    /**
     * @param id identificador da exchange na CoinMarketCap.
     * @return flow que emite o detalhe da exchange ou erro.
     */
    operator fun invoke(id: String): Flow<AppResult<ExchangeDetail>> {
        return repository.getExchangeDetail(id)
    }
}
