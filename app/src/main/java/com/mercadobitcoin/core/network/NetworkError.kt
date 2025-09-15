package com.mercadobitcoin.core.network

sealed class NetworkError {
    object TooManyRequests : NetworkError()
    object Unauthorized : NetworkError()
    object NotFound : NetworkError()
    object InternalServerError : NetworkError()
    data class Unknown(val code: Int, val message: String?) : NetworkError()
}

object HttpErrorHandler {

    fun fromCode(code: Int): String {
        return when (code) {
            401 -> "Sessão expirada. Faça login novamente."
            404 -> "O recurso solicitado não foi encontrado."
            429 -> "Estamos enfrentando instabilidade no servidor. Tente novamente mais tarde."
                    //"Você fez muitas requisições.Aguarde alguns segundos e tente novamente."
            500 -> "Estamos enfrentando instabilidade no servidor. Tente novamente mais tarde."
            1008 -> "Você excedeu o limite de taxa de solicitações HTTP da sua chave de API. Os limites de taxa são redefinidos a cada minuto."
            else ->  "Erro inesperado (HTTP:${code}).Tente novamente"
        }
    }

    fun userFriendlyMessage(error: NetworkError): String {
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

