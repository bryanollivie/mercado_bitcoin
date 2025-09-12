package com.mercadobitcoin.data.repository

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.annotation.RequiresExtension
import com.mercadobitcoin.core.network.ApiService
import com.mercadobitcoin.data.mapper.toCurrencyQuote
import com.mercadobitcoin.data.mapper.toDomainModel
import com.mercadobitcoin.domain.model.CurrencyQuote
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.domain.model.ExchangeDetail
import com.mercadobitcoin.util.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ExchangeRepository {

    override fun getExchangesWithDetails(page: Int): Flow<AppResult<List<Exchange>>> = flow {

        emit(AppResult.Loading)
        try {
            Log.e("Repository", "getExchanges")
            val listResponse = api.getExchanges(start = (page - 1) * 20 + 1)

            // Busca todos os detalhes em paralelo
            val exchangesWithDetails = coroutineScope {
                listResponse.data.map { exchangeDto ->
                    async {
                        try {
                            Log.e("Repository", "getExchangeInfo")
                            Log.e("Repository", "Busca Info ID:${exchangeDto.id.toString()}")
                            val detail = api.getExchangeInfo(exchangeDto.id.toString())
                            //val detail = getExchangeDetail(exchangeDto.id.toString())
                            Log.e("Repository", "Converte Info ID:${exchangeDto.id.toString()}")
                            exchangeDto.toDomainModel(detail.data[exchangeDto.id.toString()])
                            //exchangeDto.toDomainModel(detail.data[exchangeDto.id.toString()])
                        } catch (e: Exception) {
                            exchangeDto.toDomainModel(null)
                        }
                    }
                }.awaitAll()
            }
            emit(AppResult.Success(exchangesWithDetails))
        } catch (e: Exception) {
            emit(AppResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getExchangeDetailFull(id: String): Flow<AppResult<ExchangeDetail>> =
        flow {
            combine(
                getExchangeDetail(id),    // Requisição 1
                getExchangeCurrencies(id)  // Requisição 2
            ) { detailResult, currenciesResult ->
                when {
                    // Ambos sucesso ✅
                    detailResult is AppResult.Success && currenciesResult is AppResult.Success -> {
                        AppResult.Success(
                            detailResult.data.copy(currencies = currenciesResult.data)
                        )
                    }

                    // Detail OK, moedas falharam ⚠️
                    detailResult is AppResult.Success && currenciesResult is AppResult.Error -> {
                        AppResult.Success(detailResult.data) // Retorna sem moedas
                    }

                    // Detail falhou ❌
                    detailResult is AppResult.Error -> detailResult

                    // Loading ⏳
                    else -> AppResult.Loading
                }
            }.flowOn(Dispatchers.IO)
        }

    override fun getExchanges(page: Int): Flow<AppResult<List<Exchange>>> =
        flow {
            try {
                val response = api.getExchanges(start = page, limit = 100)
                val exchanges = response.data.map { exchangeDto ->
                    try {
                        // Busca detalhes adicionais
                        val detailResponse = api.getExchangeInfo(exchangeDto.id.toString())
                        val detailDto = detailResponse.data[exchangeDto.id.toString()]

                        // Converte para Domain Model
                        exchangeDto.toDomainModel(detailDto)
                    } catch (e: Exception) {
                        // Se falhar, retorna sem detalhes
                        exchangeDto.toDomainModel(null)
                    }
                }
                emit(AppResult.Success(exchanges))
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S
                    ) >= 7
                ) {
                    emit(AppResult.Error("${e.message}"))
                }
            }
        }.flowOn(Dispatchers.IO)
}

/* @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
 private fun handleException(e: Exception): Exception {
     return when (e) {
         is HttpException -> {
             when (e.hashCode()) {
                 401, 403 -> Exception("Invalid API key. Please check your CoinMarketCap API key configuration.")
                 429 -> Exception("Rate limit exceeded. Please try again later.")
                 404 -> Exception("Resource not found.")
                 in 500..599 -> Exception("Server error. Please try again later.")
                 else -> Exception("Network error: ${e.message}")
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
/*

class ExchangeRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ExchangeRepository {
    override suspend fun getExchanges(page: Int): AppResult<List<Exchange>> {
        return try {
            val response = api.getExchanges()
            if (response.status.errorCode == 0 ) {
                val body = response.data
                if (body != null) {
                    val urlItem = UrlItem(
                        originalUrl = body._links.self,
                        shortUrl = body._links.short,
                        alias = body.alias
                    )
                    urlHistory.add(0, urlItem)
                    NetworkResult.Success(urlItem)
                } else {
                    NetworkResult.Error("Sem resposta!")
                }
            } else {
                NetworkResult.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Erro desconhecido!")
        }
    }

    override suspend fun getExchangeDetail(id: String): AppResult<ExchangeDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getExchangeCurrencies(id: String): AppResult<List<CurrencyQuote>> {
        TODO("Not yet implemented")
    }


}
*/
