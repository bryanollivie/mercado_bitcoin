package com.mercadobitcoin.data.remote.repository

import com.mercadobitcoin.core.common.AppResult
import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.core.network.HttpErrorHandler
import com.mercadobitcoin.data.local.dao.ExchangeDao
import com.mercadobitcoin.data.mapper.toDomainModel
import com.mercadobitcoin.data.remote.dto.CurrencyDto
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: ExchangeDao
) : ExchangeRepository {


    override fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>> =
        flow {
            emit(AppResult.Loading)
            try {

                val listResponse = api.getExchanges(start = (page - 1) * 20 + 1)

                //tenta buscar o cache para atualizar a tela e dar agilidade
                /*val cached = dao.getAll()
                    .map { it.toDomain() }
                    .distinctBy { it.id }

                if (cached.isNotEmpty()) {
                    emit(AppResult.Success(cached, fromCache = true))
                }*/


                //busca os dados remoto
                val exchangesWithDetails = coroutineScope {
                    listResponse.data.map { exchangeDto ->
                        async {
                            try {
                                val detail = api.getExchangeInfo(exchangeDto.id.toString())
                                exchangeDto.toDomainModel(detail.data[exchangeDto.id.toString()])
                            } catch (e: Exception) {
                                exchangeDto.toDomainModel(null)
                            }
                        }
                    }.awaitAll()
                }
                val exchanges = exchangesWithDetails.distinctBy { it.id }

                //Cache
                //dao.insertAll(exchanges.map { it.toEntity() })

                emit(AppResult.Success(exchanges))
            } catch (e: HttpException) {

                //cache local
                val error = HttpErrorHandler.fromCode(e.code())
                emit(AppResult.Error(error))
                /*val cached = dao.getAll().map { it.toDomain() }
                if (cached.isNotEmpty()) {
                    emit(AppResult.Success(cached, fromCache = true))
                    //emit(AppResult.Success(cached, fromCache = false))
                } else {
                    emit(AppResult.Error(error))
                }*/

            }
        }.flowOn(Dispatchers.IO)

    /*override fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>> = flow {

        emit(AppResult.Loading)
        try {
            val listResponse = api.getExchanges(start = (page - 1) * 20 + 1)

            // Busca todos os detalhes em paralelo
            val exchangesWithDetails = coroutineScope {
                listResponse.data.map { exchangeDto ->
                    async {
                        try {
                            val detail = api.getExchangeInfo(exchangeDto.id.toString())
                            exchangeDto.toDomainModel(detail.data[exchangeDto.id.toString()])
                        } catch (e: Exception) {
                            exchangeDto.toDomainModel(null)
                        }
                    }
                }.awaitAll()
            }
            emit(AppResult.Success(exchangesWithDetails))
        } catch (e: HttpException) {
            val error = HttpErrorHandler.fromCode(e.code(), e.message())
            val friendlyMessage = HttpErrorHandler.userFriendlyMessage(error)
            emit(AppResult.Error(friendlyMessage))
        }

    }.flowOn(Dispatchers.IO)*/

    override fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>> =
        flow {
            try {
                val response = api.getExchangeInfo(id)
                val detailDto = response.data[id]
                if (detailDto != null) {
                    val exchangeDetail = detailDto.toDomainModel()
                    emit(AppResult.Success(exchangeDetail))
                } else {
                    emit(AppResult.Error(Exception("not found with ID: $id").toString()))
                }

            } catch (e: HttpException) {
                val error = HttpErrorHandler.fromCode(e.code())
                emit(AppResult.Error(error))
            }
        }.flowOn(Dispatchers.IO)

    override fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyDto>>> =
        flow {
            emit(AppResult.Loading)
            try {
                val response = api.getExchangeAssets(id)
                val currencies = response.data.mapNotNull {
                    it.currency
                }
                if (currencies != null) {
                    emit(AppResult.Success(currencies.toList()))
                }

            } catch (e: HttpException) {
                val error = HttpErrorHandler.fromCode(e.code())
                emit(AppResult.Error(error))
            }
        }.flowOn(Dispatchers.IO)

}
