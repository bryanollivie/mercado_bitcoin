package com.mercadobitcoin.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mercadobitcoin.BuildConfig
import com.mercadobitcoin.core.common.DefaultDispatcherProvider
import com.mercadobitcoin.core.common.DispatcherProvider
import com.mercadobitcoin.core.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Modulo Hilt que configura toda a stack de rede da aplicacao.
 * Todos os componentes sao Singleton para reutilizacao ao longo do ciclo de vida do app.
 *
 * Componentes fornecidos:
 * - [Json] configurado para ignorar campos desconhecidos da API
 * - Interceptors para autenticacao (API key) e logging (apenas em debug)
 * - [OkHttpClient] com timeouts de 30s
 * - [Retrofit] apontando para a CoinMarketCap API
 * - [ApiService] criado via Retrofit
 * - [DispatcherProvider] para injecao de dispatchers testaveis
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** Configura o parser JSON com tolerancia a campos desconhecidos da API. */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Interceptor que adiciona o header de autenticacao da CoinMarketCap API
     * em todas as requisicoes HTTP.
     */
    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("X-CMC_PRO_API_KEY", BuildConfig.CMC_API_KEY)
            .header("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    /** Interceptor de logging: exibe body completo em debug, desativado em release. */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /** Cliente HTTP com interceptors de auth e logging, e timeouts de 30 segundos. */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /** Instancia Retrofit configurada com base URL e conversor kotlinx.serialization. */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /** Cria a implementacao do [ApiService] a partir do Retrofit. */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    /** Fornece o provider de dispatchers (substituivel em testes unitarios). */
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}
