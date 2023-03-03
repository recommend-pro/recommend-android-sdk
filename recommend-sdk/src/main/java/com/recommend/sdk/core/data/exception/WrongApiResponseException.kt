package com.recommend.sdk.core.data.exception

class WrongApiResponseException(rawResponse: String): Throwable("Wrong API response: $rawResponse")
