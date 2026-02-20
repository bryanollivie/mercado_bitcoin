package com.mercadobitcoin.domain.usecase

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.repository.ExchangeRepository
import com.mercadobitcoin.domain.model.Exchange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para buscar a lista de exchanges com detalhes.
 * Encapsula a regra de paginacao e delega ao repositorio a
 * estrategia de cache-first + dados remotos.
 *
 * Chamado pelo [ExchangesViewModel] para carregar a tela principal.
 */
class GetExchangesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    /**
     * @param page numero da pagina (1-based) para paginacao.
     * @return flow reativo que emite Loading -> [cache] -> dados frescos ou erro.
     */
    suspend operator fun invoke(page: Int = 1): Flow<AppResult<List<Exchange>>> {
        return repository.getExchangesWithDetails(page)
    }
}
