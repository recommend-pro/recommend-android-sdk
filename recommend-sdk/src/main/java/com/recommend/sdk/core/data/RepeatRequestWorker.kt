package com.recommend.sdk.core.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.recommend.sdk.core.data.api.ApiServiceBuilder
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.RecommendLogger
import okhttp3.Request
import okhttp3.RequestBody

class RepeatRequestWorker(private val appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    companion object {
        const val SERIALIZED_REPEATABLE_TASK_PARAMS = "SERIALIZED_REPEATABLE_TASK"
    }

    override fun doWork(): Result {
        Log.d(RecommendLogger.LOG_TAG, "Request worker start work")
        val rawRepeatableApiTask = inputData.getString(SERIALIZED_REPEATABLE_TASK_PARAMS)
        if (rawRepeatableApiTask !is String) {
            return Result.failure()
        }
        val repeatableApiTask = ApiTask.RepeatableApiTask.deserializedFromJson(rawRepeatableApiTask)
        repeatableApiTask.incrementAttemptNumber()
        val serializedRequest = repeatableApiTask.request

        val recommendLogger = RecommendLogger()

        val okHttpClient = ApiServiceBuilder.getApiClient(ApiHelper(appContext), recommendLogger)

        val request = Request.Builder()
        request.url(serializedRequest.url)

        val requestBody = if (serializedRequest.body.isNotEmpty()) {
            RequestBody.create(null, serializedRequest.body)
        } else {
            null
        }

        request.method(serializedRequest.method, requestBody)

       if (serializedRequest.headers.isNotEmpty()) {
           serializedRequest.headers.forEach {
               request.header(it.key, it.value)
           }
       }

        Log.d(RecommendLogger.LOG_TAG, "Request worker is repeating request: $request")

        val apiManager = ApiManager(appContext, recommendLogger)

        return try {
            val response = okHttpClient.newCall(request.build()).execute()
            Log.d(RecommendLogger.LOG_TAG, "Request worker got response: ${response.body()?.string()} with code: ${response.code()}")
            Result.success()
        } catch (e: Throwable) {
            apiManager.handleError(repeatableApiTask, e)
            Log.d(RecommendLogger.LOG_TAG, "Request worker got error: $e")
            Result.failure()
        }
    }
}
