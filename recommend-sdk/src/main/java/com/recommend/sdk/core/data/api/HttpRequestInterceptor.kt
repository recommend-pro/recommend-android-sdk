package com.recommend.sdk.core.data.api

import com.recommend.sdk.core.data.exception.NoConnectionException
import com.recommend.sdk.core.data.util.ApiHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpRequestInterceptor(private val apiHelper: ApiHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!apiHelper.isInternetAvailable()) {
            throw NoConnectionException()
        }

        val newRequest: Request = request.newBuilder()
            .header("content-type", "application/json")
            .header("accept", "application/json")
            .header("cache-control", "no-cache")
            .url(request.url())
            .build()

        return chain.proceed(newRequest)
    }
}
