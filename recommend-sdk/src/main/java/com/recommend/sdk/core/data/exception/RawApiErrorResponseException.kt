package com.recommend.sdk.core.data.exception

class RawApiErrorResponseException(
    val rawResponse: String
): Throwable()
