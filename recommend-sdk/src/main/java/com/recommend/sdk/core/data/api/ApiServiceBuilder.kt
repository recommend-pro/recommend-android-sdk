package com.recommend.sdk.core.data.api

import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.JsonHelper
import com.recommend.sdk.core.util.RecommendLogger
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceBuilder {
    companion object {
        fun <T> getService(
            applicationId: String,
            apiHost: String,
            apiHelper: ApiHelper,
            apiServiceInterface: Class<T>,
            recommendLogger: RecommendLogger
        ): T {
            val retrofit = retrofit2.Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(JsonHelper.getGson()))
                .client(getApiClient(apiHelper, recommendLogger))
                .baseUrl("$apiHost/$applicationId/")
                .build()

            return retrofit.create(apiServiceInterface)
        }

        fun getApiClient(apiHelper: ApiHelper, recommendLogger: RecommendLogger): OkHttpClient {
            return OkHttpClient()
                .newBuilder()
                .addInterceptor(HttpRequestInterceptor(apiHelper))
                .addInterceptor(HttpResponseInterceptor(recommendLogger))
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                .build()
        }
    }
}
