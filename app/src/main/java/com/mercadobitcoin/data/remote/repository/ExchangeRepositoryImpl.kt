package com.mercadobitcoin.data.remote.repository

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.core.common.DispatcherProvider
import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.core.network.HttpErrorHandler
import com.mercadobitcoin.data.local.dao.ExchangeDao
import com.mercadobitcoin.data.mapper.toDomain
import com.mercadobitcoin.data.mapper.toDomainModel
import com.mercadobitcoin.data.mapper.toEntity
import com.mercadobitcoin.data.remote.dto.CurrencyDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementacao do repositorio que combina dados da API CoinMarketCap
 * com cache local (Room) para oferecer uma experiencia offline-first.
 *
 * Estrategia de cache:
 * 1. Emite dados do cache local imediatamente (se existirem) para agilizar a UI.
 * 2. Busca dados frescos da API em paralelo.
 * 3. Atualiza o cache com os dados novos (operacao atomica via @Transaction).
 * 4. Em caso de falha HTTP, faz fallback para o cache; se vazio, emite erro.
 *
 * @param api servico Retrofit para chamadas a CoinMarketCap.
 * @param dao DAO Room para persistencia local das exchanges.
 * @param dispatcherProvider abstrai dispatchers para testabilidade.
 * @param httpErrorHandler converte codigos HTTP em mensagens localizadas.
 */
@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: ExchangeDao,
    private val dispatcherProvider: DispatcherProvider,
    private val httpErrorHandler: HttpErrorHandler
) : ExchangeRepository {

    /**
     * Busca exchanges com detalhes, usando a estrategia cache-first descrita acima.
     *
     * Para cada exchange retornada pela listagem, busca os detalhes em paralelo
     * com [async]/[awaitAll], aplicando timeout individual de 10s por chamada.
     * Se o detalhe de uma exchange falhar ou expirar, ela e incluida sem detalhes.
     */
    override fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>> =
        flow {
            emit(AppResult.Loading)
            try {
                // Busca a listagem paginada da API
                val listResponse = api.getExchanges(start = (page - 1) * 20 + 1)

                // Emite cache local para resposta imediata na UI
                val cached = dao.getAll()
                    .map { it.toDomain() }
                    .distinctBy { it.id }

                if (cached.isNotEmpty()) {
                    emit(AppResult.Success(cached, fromCache = true))
                }

                // Busca detalhes de cada exchange em paralelo com timeout individual
                val exchangesWithDetails = coroutineScope {
                    listResponse.data.map { exchangeDto ->
                        async {
                            withTimeoutOrNull(10_000L) {
                                try {
                                    val detail =
                                        api.getExchangeInfo(exchangeDto.id.toString())
                                    exchangeDto.toDomainModel(detail.data[exchangeDto.id.toString()])
                                } catch (e: Exception) {
                                    exchangeDto.toDomainModel(null)
                                }
                            } ?: exchangeDto.toDomainModel(null)
                        }
                    }.awaitAll()
                }
                val exchanges = exchangesWithDetails.distinctBy { it.id }

                // Atualiza cache atomicamente (clear + insert dentro de @Transaction)
                dao.clearAndInsertAll(exchanges.map { it.toEntity() })

                emit(AppResult.Success(exchanges))
            } catch (e: HttpException) {
                // Fallback: tenta entregar dados do cache em caso de erro HTTP
                val error = httpErrorHandler.fromCode(e.code())
                val cached = dao.getAll().map { it.toDomain() }

                if (cached.isNotEmpty()) {
                    emit(AppResult.Success(cached, fromCache = true))
                } else {
                    emit(AppResult.Error(error))
                }
            }
        }.flowOn(dispatcherProvider.io)

    /**
     * Busca detalhes de uma exchange especifica pelo ID.
     * Retorna erro localizado se a exchange nao for encontrada no mapa de resposta.
     */
    override fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>> =
        flow {
            try {
                val response = api.getExchangeInfo(id)
                val detailDto = response.data[id]
                if (detailDto != null) {
                    val exchangeDetail = detailDto.toDomainModel()
                    emit(AppResult.Success(exchangeDetail))
                } else {
                    emit(AppResult.Error("Exchange not found with ID: $id"))
                }
            } catch (e: HttpException) {
                val error = httpErrorHandler.fromCode(e.code())
                emit(AppResult.Error(error))
            }
        }.flowOn(dispatcherProvider.io)

    /**
     * Busca as criptomoedas (assets) negociadas em uma exchange.
     * Extrai apenas o campo [CurrencyDto] de cada asset, ignorando nulls.
     */
    override fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyDto>>> =
        flow {
            emit(AppResult.Loading)
            try {
                val response = api.getExchangeAssets(id)
                val currencies = response.data.mapNotNull {
                    it.currency
                }
                emit(AppResult.Success(currencies.toList()))
            } catch (e: HttpException) {
                val error = httpErrorHandler.fromCode(e.code())
                emit(AppResult.Error(error))
            }
        }.flowOn(dispatcherProvider.io)
}
