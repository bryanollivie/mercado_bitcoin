package com.mercadobitcoin.core.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

sealed class AppResult<out T> {
    data class Success<T>(val data: T): AppResult<T>()
    data class Error(val message: String, val cause: Throwable? = null): AppResult<Nothing>()
    object Loading : AppResult<Nothing>()
}