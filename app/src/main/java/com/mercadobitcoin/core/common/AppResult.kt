package com.mercadobitcoin.core.common

/**
 * Wrapper generico para representar o estado de uma operacao assincrona.
 * Utilizado em toda a camada de dados e apresentacao para padronizar
 * o fluxo de Loading -> Success/Error.
 *
 * @param T tipo do dado retornado em caso de sucesso.
 */
sealed class AppResult<out T> {
    /**
     * Operacao concluida com sucesso.
     * @param data dados retornados.
     * @param fromCache indica se os dados vieram do cache local (fallback de rede).
     */
    data class Success<T>(val data: T, val fromCache: Boolean? = false) : AppResult<T>()

    /**
     * Operacao falhou.
     * @param message mensagem de erro ja localizada para exibicao ao usuario.
     * @param cause throwable original, caso disponivel, para fins de log/debug.
     */
    data class Error(val message: String, val cause: Throwable? = null) : AppResult<Nothing>()

    /** Estado de carregamento — emitido antes do resultado final. */
    object Loading : AppResult<Nothing>()
}
