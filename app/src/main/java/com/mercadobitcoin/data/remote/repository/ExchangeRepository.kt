package com.mercadobitcoin.data.remote.repository

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.data.remote.dto.CurrencyDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow

/**
 * Contrato da camada de dados para acesso a informacoes de exchanges.
 * Define os fluxos reativos (Flow) que emitem [AppResult] para representar
 * Loading, Success e Error de forma padronizada.
 *
 * A implementacao concreta ([ExchangeRepositoryImpl]) combina dados da API remota
 * com cache local (Room) seguindo a estrategia cache-first com fallback.
 */
interface ExchangeRepository {

    /**
     * Busca os detalhes de uma exchange especifica.
     * @param id identificador da exchange na CoinMarketCap.
     * @return flow que emite o detalhe da exchange ou erro.
     */
    fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>>

    /**
     * Busca as criptomoedas disponiveis em uma exchange.
     * @param id identificador da exchange.
     * @return flow que emite Loading -> lista de moedas ou erro.
     */
    fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyDto>>>

    /**
     * Busca a lista de exchanges com seus detalhes (logo, volume, data de lancamento).
     * Estrategia: emite cache local primeiro (se disponivel), depois busca dados frescos
     * da API, e em caso de falha HTTP, faz fallback para o cache.
     *
     * @param page numero da pagina para paginacao (1-based).
     * @return flow que emite Loading -> [cache] -> dados frescos ou erro.
     */
    fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>>
}
