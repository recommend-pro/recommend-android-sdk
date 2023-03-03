package com.recommend.sdk.core.data

import com.recommend.sdk.core.data.model.response.DefaultErrorResponse
import com.recommend.sdk.core.util.JsonHelper
import okio.Buffer
import java.util.*
import java.util.concurrent.TimeUnit


class ApiTask<T> (
    val requestTask: RequestTask<T>,
    val repeatConfig: RepeatConfig = RepeatConfig(),
    val apiErrorResponseType: Class<*> = DefaultErrorResponse::class.java
) {
    private val createOnTime: Long = System.currentTimeMillis() / 1000

    data class RepeatConfig(
        val needRepeatWhenNoInternetConnection: Boolean = false,
        val needRepeatOnUnsuccess: Boolean = false,
        /*
        The repetition pattern defines interval (in seconds) between attempts.
        The first value defines interval between failed response and first attempt, the second value defines interval between first attempt and second attempt etc.
        The default value is null. This means that the request will be repeated until it's completed or destroyed for other reasons.
         */
        val repeatPattern: List<Pair<Long, TimeUnit>>? = null,
        /*
        The maximum interval between creation time and last attempt.
        If interval between the current time and the creation time is greater than repeat interval, this request will be destroy.
        Null value means that the request will be repeated until it's completed or destroyed for other reasons.
         */
        val repeatInterval: Long? = 7,
        val repeatIntervalTimeUnit: TimeUnit = TimeUnit.DAYS
    )

    data class RepeatableApiTask (
        val request: SerializedRequest,
        val repeatConfig: RepeatConfig,
        val createOnTime: Long,
        val uuid: String = UUID.randomUUID().toString()
    ) {
        private var attemptNumber: Int = 1

        data class SerializedRequest (
                val url: String,
                val method: String,
                val headers: Map<String, String>,
                val body: String
        )

        fun getAttemptNumber(): Int = attemptNumber
        fun incrementAttemptNumber() {
            attemptNumber += 1
        }

        companion object {
            //Default interval between attempts in seconds
            const val DEFAULT_ATTEMPT_INTERVAL: Long = 60

            fun deserializedFromJson(serializedApiTask: String): RepeatableApiTask {
                return JsonHelper.fromJson(serializedApiTask, RepeatableApiTask::class.java)
            }
        }

        fun serializedToJson(): String {
            return JsonHelper.toJson(this)
        }
    }

    fun getUUID(): String {
        return requestTask.uuid
    }

    fun toRepeatableApiTask(): RepeatableApiTask {
        val headers = requestTask.call.request().headers()
        val serializedHeaders = mutableMapOf<String, String>()

        headers.names().forEach { name ->
            serializedHeaders[name] = headers.get(name).toString()
        }

        val serializedBody = try {
            val copy = requestTask.call.request().newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Throwable) {
            ""
        }

        return RepeatableApiTask(
                RepeatableApiTask.SerializedRequest(
                    requestTask.call.request().url().toString(),
                    requestTask.call.request().method(),
                        serializedHeaders,
                        serializedBody
                ),
                repeatConfig,
                createOnTime,
                requestTask.uuid
        )
    }
}
