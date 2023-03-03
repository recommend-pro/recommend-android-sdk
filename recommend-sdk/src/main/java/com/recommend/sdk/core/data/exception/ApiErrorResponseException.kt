package com.recommend.sdk.core.data.exception

class ApiErrorResponseException(val errorResponse: Any): Throwable("Error response: $errorResponse")
