package com.mercadobitcoin.core.network

import com.mercadobitcoin.data.remote.dto.AssetsResponse
import com.mercadobitcoin.data.remote.dto.ExchangeDetailResponse
import com.mercadobitcoin.data.remote.dto.ExchangeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface Retrofit que define os endpoints da API CoinMarketCap.
 * Todas as chamadas sao suspend functions para uso com coroutines.
 * A autenticacao (header X-CMC_PRO_API_KEY) e adicionada via interceptor no [NetworkModule].
 */
interface ApiService {

    /**
     * Lista as exchanges disponiveis com paginacao.
     * @param start posicao inicial (1-based) para paginacao.
     * @param limit quantidade de registros por pagina.
     * @param sort criterio de ordenacao.
     */
    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 15,
        @Query("sort") sort: String = "id"
    ): ExchangeListResponse

    /**
     * Busca informacoes detalhadas de uma exchange (logo, fees, website, etc).
     * @param id identificador unico da exchange na CoinMarketCap.
     * @return mapa de ID -> detalhes da exchange.
     */
    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: String
    ): ExchangeDetailResponse

    /**
     * Lista os ativos (criptomoedas) disponiveis em uma exchange.
     * @param id identificador unico da exchange.
     * @return lista de ativos com preco em USD.
     */
    @GET("v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: String
    ): AssetsResponse
}
