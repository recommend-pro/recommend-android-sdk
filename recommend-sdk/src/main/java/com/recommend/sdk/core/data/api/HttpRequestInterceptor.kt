package com.recommend.sdk.core.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpRequestInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest: Request = request.newBuilder()
            .header("content-type", "application/json")
            .header("accept", "application/json")
            .header("cache-control", "no-cache")
            .url(request.url())
            .build()

        return chain.proceed(newRequest)
    }
}
