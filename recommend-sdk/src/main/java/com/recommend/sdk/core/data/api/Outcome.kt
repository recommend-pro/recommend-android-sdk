package com.recommend.sdk.core.data.api

sealed class Outcome<T> {
    data class Progress<T>(val data: T? = null) : Outcome<T>()
    data class Success<T>(val data: T) : Outcome<T>()
    data class Failure<T>(val e: Throwable) : Outcome<T>()
}

