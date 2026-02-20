package com.mercadobitcoin.core.common

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Abstrai o acesso a string resources do Android para permitir
 * uso em camadas que nao possuem Context (ex.: Repository, UseCase)
 * e substituicao em testes unitarios via mock.
 */
interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg args: Any): String
}

/** Implementacao concreta que delega para [Context.getString]. */
@Singleton
class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(@StringRes resId: Int): String = context.getString(resId)
    override fun getString(@StringRes resId: Int, vararg args: Any): String =
        context.getString(resId, *args)
}
