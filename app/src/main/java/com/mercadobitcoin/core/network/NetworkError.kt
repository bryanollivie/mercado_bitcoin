package com.mercadobitcoin.core.network
import java.util.Locale

sealed class NetworkError {
    object TooManyRequests : NetworkError()
    object Unauthorized : NetworkError()
    object NotFound : NetworkError()
    object InternalServerError : NetworkError()
    data class Unknown(val code: Int, val message: String?) : NetworkError()
}

object HttpErrorHandler {
    fun fromCode(code: Int, message: String? = null): NetworkError {
        return when (code) {
            401 -> NetworkError.Unauthorized
            404 -> NetworkError.NotFound
            429 -> NetworkError.TooManyRequests
            500 -> NetworkError.InternalServerError
            else -> NetworkError.Unknown(code, message)
        }
    }

    fun userFriendlyMessage(error: NetworkError, locale: Locale = Locale.getDefault()): String {
        return when (error) {
        //return when (locale.language) {
           // when (error) {
                is NetworkError.Unauthorized ->
                    "Sessão expirada. Faça login novamente."
                is NetworkError.NotFound ->
                    "O recurso solicitado não foi encontrado."
                is NetworkError.TooManyRequests ->
                    "Estamos enfrentando instabilidade no servidor. Tente novamente mais tarde."
                    //"Você fez muitas requisições.Aguarde alguns segundos e tente novamente."
                is NetworkError.InternalServerError ->
                    "Estamos enfrentando instabilidade no servidor. Tente novamente mais tarde."
                is NetworkError.Unknown ->
                    "Erro inesperado (${error.code}). ${error.message ?: "Tente novamente"}"
            }
            /*else -> when (error) { // padrão inglês
                is NetworkError.Unauthorized ->
                    "Session expired. Please log in again."
                is NetworkError.NotFound ->
                    "The requested resource was not found."
                is NetworkError.TooManyRequests ->
                    "Too many requests. Please wait a few seconds and try again."
                is NetworkError.InternalServerError ->
                    "We are experiencing server issues. Please try again later."
                is NetworkError.Unknown ->
                    "Unexpected error (${error.code}). ${error.message ?: "Please try again"}"
            }*/
        }
    }

