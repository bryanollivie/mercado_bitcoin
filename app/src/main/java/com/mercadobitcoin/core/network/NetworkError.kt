package com.mercadobitcoin.core.network

import com.mercadobitcoin.R
import com.mercadobitcoin.core.common.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Representa os tipos de erro de rede conhecidos pela aplicacao.
 * Utilizado para tipagem segura em tratamento de erros sem depender de strings.
 */
sealed class NetworkError {
    object TooManyRequests : NetworkError()
    object Unauthorized : NetworkError()
    object NotFound : NetworkError()
    object InternalServerError : NetworkError()
    data class Unknown(val code: Int, val message: String?) : NetworkError()
}

/**
 * Converte codigos HTTP em mensagens de erro localizadas para o usuario.
 * Utiliza [ResourceProvider] para resolver strings de `strings.xml`,
 * garantindo suporte a i18n sem acoplar a camada de dados ao Android Context.
 */
@Singleton
class HttpErrorHandler @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    /**
     * Mapeia um codigo HTTP para uma mensagem de erro amigavel e localizada.
     *
     * @param code codigo HTTP da resposta (ex.: 401, 404, 429, 500).
     * @return mensagem localizada pronta para exibicao na UI.
     */
    fun fromCode(code: Int): String {
        return when (code) {
            401 -> resourceProvider.getString(R.string.error_session_expired)
            404 -> resourceProvider.getString(R.string.error_not_found)
            429 -> resourceProvider.getString(R.string.error_too_many_requests)
            500 -> resourceProvider.getString(R.string.error_server)
            1008 -> resourceProvider.getString(R.string.error_rate_limit)
            else -> resourceProvider.getString(R.string.error_unexpected, code)
        }
    }
}
