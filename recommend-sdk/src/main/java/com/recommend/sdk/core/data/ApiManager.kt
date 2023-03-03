package com.recommend.sdk.core.data

import android.content.Context
import androidx.work.*
import com.recommend.sdk.core.data.exception.ApiErrorResponseException
import com.recommend.sdk.core.data.exception.NoConnectionException
import com.recommend.sdk.core.data.exception.RawApiErrorResponseException
import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.util.JsonHelper
import com.recommend.sdk.core.util.RecommendLogger
import java.util.concurrent.TimeUnit

class ApiManager(private val context: Context, private val recommendLogger: RecommendLogger) {
    private val requestManager = RequestManager(recommendLogger)

    fun <T> processTask(apiTask: ApiTask<T>) {
        requestManager.executeRequest(
            RequestTask(
                apiTask.requestTask.call,
                DataListener(
                    { response, requestTask ->
                        apiTask.requestTask.dataListener?.successCallback?.let { it(response, requestTask) }
                    },
                    { error, requestTask ->
                        if (error is RawApiErrorResponseException) {
                            try {
                                val errorResponse = JsonHelper.fromJson(
                                    error.rawResponse,
                                    apiTask.apiErrorResponseType
                                )
                                apiTask.requestTask.dataListener?.errorCallback?.let {
                                    it(ApiErrorResponseException(errorResponse), requestTask)
                                }
                            } catch (e: Throwable) {
                                apiTask.requestTask.dataListener?.errorCallback?.let { it(e, requestTask) }
                            }
                        } else {
                            apiTask.requestTask.dataListener?.errorCallback?.let { it(error, requestTask) }
                        }
                        handleError(apiTask.toRepeatableApiTask(), error)
                    }
                ),
                apiTask.requestTask.inTurn
            )
        )
    }

    fun handleError(repeatableApiTask: ApiTask.RepeatableApiTask, error: Throwable) {
        if ((isNetworkError(error) || error is RawApiErrorResponseException)
            && needRepeatTask(repeatableApiTask, error)) {
            createWorkRequest(repeatableApiTask)
        }
    }

    private fun createWorkRequest(repeatableApiTask: ApiTask.RepeatableApiTask) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val interval: Pair<Long, TimeUnit> = if (repeatableApiTask.repeatConfig.repeatPattern != null) {
            val attemptNumber = repeatableApiTask.getAttemptNumber()
            if (attemptNumber <= repeatableApiTask.repeatConfig.repeatPattern.size) {
                repeatableApiTask.repeatConfig.repeatPattern[attemptNumber - 1]
            } else {
                return
            }
        } else {
            Pair(ApiTask.RepeatableApiTask.DEFAULT_ATTEMPT_INTERVAL, TimeUnit.SECONDS)
        }

        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<RepeatRequestWorker>()
                .setInputData(
                    workDataOf(
                        RepeatRequestWorker.SERIALIZED_REPEATABLE_TASK_PARAMS to repeatableApiTask.serializedToJson()
                    )
                )
                .setInitialDelay(interval.first, interval.second)
                .setConstraints(constraints)
                .build()
        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }

    private fun needRepeatTask(
        repeatableApiTask: ApiTask.RepeatableApiTask,
        error: Throwable
    ): Boolean {
        val repeatConfig = repeatableApiTask.repeatConfig
        val repeatInterval = repeatConfig.repeatInterval
        if (repeatInterval != null) {
            val lastRepeatTime = repeatConfig.repeatIntervalTimeUnit.toSeconds(repeatInterval) + repeatableApiTask.createOnTime
            val currentTime = System.currentTimeMillis() / 1000
            if (currentTime >= lastRepeatTime) {
                return false
            }
        }

        return if ((isNetworkError(error) && repeatableApiTask.repeatConfig.needRepeatWhenNoInternetConnection)
            || (error is ApiErrorResponseException && repeatableApiTask.repeatConfig.needRepeatOnUnsuccess)) {
            if (repeatConfig.repeatPattern != null) {
                repeatableApiTask.getAttemptNumber() <= repeatConfig.repeatPattern.size
            } else {
                true
            }
        } else {
            false
        }
    }

    private fun isNetworkError(e: Throwable): Boolean {
        val isInJavaNetPackage = e.javaClass.`package`?.name?.startsWith("java.net") ?: false

        return e is NoConnectionException || isInJavaNetPackage
    }
}
