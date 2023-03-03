package com.recommend.sdk.core.data.api

import com.recommend.sdk.core.util.RecommendLogger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class HttpResponseInterceptor(private val logger: RecommendLogger): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val body: ResponseBody = response.peekBody(Long.MAX_VALUE)
        val content: String = body.string()
        logger.logRawResponse(content)

        return response
    }
}
