package com.recommend.sdk.core.data

import com.recommend.sdk.core.data.exception.NoConnectionException
import com.recommend.sdk.core.data.exception.RawApiErrorResponseException
import com.recommend.sdk.core.data.exception.WrongApiResponseException
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.RecommendLogger
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response


class RequestManager(private val logger: RecommendLogger, private val apiHelper: ApiHelper) {
    private var taskQueue: LinkedList<RequestTask<*>> = LinkedList<RequestTask<*>>()
    private var isTaskProcessing: Boolean = false

    fun <T> executeRequest(requestTask: RequestTask<T>) {
        if (requestTask.inTurn) {
            if (!isTaskProcessing) {
                execute(requestTask)
            } else {
                taskQueue.add(requestTask)
            }
        } else {
            execute(requestTask)
        }
    }

    private fun <T> execute(requestTask: RequestTask<T>) {
        if (!apiHelper.isInternetAvailable()) {
            isTaskProcessing = false
            val exception = NoConnectionException()
            logger.logRequestException(exception)
            requestTask.dataListener?.errorCallback?.let { it(exception, requestTask) }
            processNextTask()
        }

        isTaskProcessing = true
        logger.logRawRequest(requestTask)
        requestTask.call.enqueue(object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let { logger.logResponse(it) }
                    requestTask.dataListener?.successCallback?.let { it(response.body(), requestTask) }
                    isTaskProcessing = false
                    processNextTask()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val exception = if (errorBody != null) {
                        RawApiErrorResponseException(errorBody)
                    } else {
                        WrongApiResponseException("")
                    }
                    logger.logResponse(errorBody ?: "")
                    requestTask.dataListener?.errorCallback?.let { it(exception, requestTask) }
                    isTaskProcessing = false
                    processNextTask()
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                isTaskProcessing = false
                logger.logRequestException(t)
                requestTask.dataListener?.errorCallback?.let { it(t, requestTask) }
                processNextTask()
            }
        })
    }

    private fun processNextTask() {
        if (taskQueue.isNotEmpty()) {
            val task = taskQueue.pop()
            if (task != null) {
                execute(task)
            }
        }
    }
}
