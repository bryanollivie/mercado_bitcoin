package com.mercadobitcoin.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Abstrai os dispatchers de coroutines para permitir substituicao em testes.
 * Em testes unitarios, injeta-se uma implementacao com [UnconfinedTestDispatcher]
 * para que os flows executem de forma sincrona.
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}

/** Implementacao padrao que mapeia para os dispatchers reais do Android. */
class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
}
