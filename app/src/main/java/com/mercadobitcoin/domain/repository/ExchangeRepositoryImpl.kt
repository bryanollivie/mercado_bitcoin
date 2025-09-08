/*
package com.mercadobitcoin.domain.repository

import com.mercadobitcoin.data.remote.api.ApiService
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService

) : ExchangeRepository {

    override suspend fun getExchanges(page: Int): Flow<Result<List<Exchange>>> = flow {
        emit(Result.Loading)
        try {
            val start = (page - 1) * 20 + 1
            val response = api.getExchanges(start = start, limit = 20)

            // Para cada exchange, buscar detalhes para obter logo, volume e data
            val exchanges = response.data.map { exchangeDto ->
                try {
                    // Busca detalhes adicionais da exchange
                    val detailResponse = api.getExchangeInfo(exchangeDto.id.toString())
                    val detailDto = detailResponse.data[exchangeDto.id.toString()]
                    exchangeDto.toDomainModel(detailDto)
                } catch (e: Exception) {
                    // Se falhar ao buscar detalhes, retorna apenas com dados básicos
                    exchangeDto.toDomainModel(null)
                }
            }

            emit(Result.Success(exchanges))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getExchangeDetail(id: String): Flow<Result<ExchangeDetail>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getExchangeInfo(id)
            val detailDto = response.data[id]

            if (detailDto != null) {
                val exchangeDetail = detailDto.toDomainModel()
                emit(Result.Success(exchangeDetail))
            } else {
                emit(Result.Error(Exception("Exchange not found with ID: $id")))
            }
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getExchangeCurrencies(id: String): Flow<Result<List<CurrencyQuote>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getExchangeAssets(id)

            // A API retorna um mapa com o ID da exchange como chave
            val currencies = response.data[id]
                ?.mapNotNull { assetDto ->
                    // Converte apenas assets que têm preço em USD
                    assetDto.toCurrencyQuote()
                }
                ?.distinctBy { it.name } // Remove duplicatas por nome
                ?.sortedByDescending { it.priceUsd } // Ordena por preço (maior primeiro)
                ?: emptyList()

            emit(Result.Success(currencies))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }.flowOn(Dispatchers.IO)

    */
/**
     * Trata exceções e retorna mensagens de erro apropriadas
     *//*

    private fun handleException(e: Exception): Exception {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401, 403 -> Exception("Invalid API key. Please check your CoinMarketCap API key configuration.")
                    429 -> Exception("Rate limit exceeded. Please try again later.")
                    404 -> Exception("Resource not found.")
                    in 500..599 -> Exception("Server error. Please try again later.")
                    else -> Exception("Network error: ${e.message()}")
                }
            }
            is SocketTimeoutException -> {
                Exception("Connection timeout. Please check your internet connection.")
            }
            is UnknownHostException -> {
                Exception("Unable to reach server. Please check your internet connection.")
            }
            is IOException -> {
                Exception("Network error. Please check your internet connection.")
            }
            else -> {
                Exception(e.message ?: "An unexpected error occurred.")
            }
        }
    }
}*/
