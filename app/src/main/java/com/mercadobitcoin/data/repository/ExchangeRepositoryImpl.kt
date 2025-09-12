package com.mercadobitcoin.data.repository

import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.data.mapper.toCurrencyQuote
import com.mercadobitcoin.data.mapper.toDomainModel
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.core.common.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ExchangeRepository {

    override fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>> = flow {

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
        } catch (e: Exception) {
            emit(AppResult.Error(e.message ?: "Erro desconhecido"))
        }
    }.flowOn(Dispatchers.IO)


    override fun getExchangeDetail(id: String): Flow<AppResult<ExchangeDetail>> =
        flow {
            try {
                val response = api.getExchangeInfo(id)
                val detailDto = response.data[id]
                if (detailDto != null) {
                    val exchangeDetail = detailDto.toDomainModel()
                    emit(AppResult.Success(exchangeDetail))
                } else {
                    emit(AppResult.Error(Exception("Exchange not found with ID: $id").toString()))
                }

            } catch (e: Exception) {
                emit(AppResult.Error(e.message ?: "Erro desconhecido"))
            }
        }.flowOn(Dispatchers.IO)


    override fun getExchangeCurrencies(id: String): Flow<AppResult<List<CurrencyQuote>>> =
        flow {
            emit(AppResult.Loading)
            try {
                val response = api.getExchangeAssets(id)

                val currencies = response.data[id]
                    ?.mapNotNull { assetDto ->
                        assetDto.toCurrencyQuote()
                    }
                    ?.distinctBy { it.name }
                    ?.sortedByDescending { it.priceUsd }
                    ?: emptyList()

                emit(AppResult.Success(currencies))
            } catch (e: Exception) {
                emit(AppResult.Error(e.message ?: "Erro desconhecido"))
            }
        }.flowOn(Dispatchers.IO)
}
