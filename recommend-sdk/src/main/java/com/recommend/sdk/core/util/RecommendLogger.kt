package com.recommend.sdk.core.util

import android.util.Log
import com.recommend.sdk.core.data.ApiTask
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.model.CurrentState
import com.recommend.sdk.device.data.model.activity.BaseActivity
import com.recommend.sdk.messaging.data.model.MessagingPushSubscriptionStatus
import okio.Buffer

class RecommendLogger {
    private val loggingBehavior: MutableList<LoggingBehavior> = mutableListOf()

    companion object {
        const val LOG_TAG = "RECOMMEND_SDK"
    }

    fun addLoggingBehavior(behavior: LoggingBehavior) {
        if (behavior !in loggingBehavior) {
            loggingBehavior.add(behavior)
        }
    }

    fun removeLoggingBehavior(behavior: LoggingBehavior) {
        loggingBehavior.remove(behavior)
    }

    fun logCheckDeviceNotificationStatusWorkerProgress(
        state: String
    ) {
        if (LoggingBehavior.MESSAGING in loggingBehavior) {
            Log.d(LOG_TAG, state)
        }
    }

    fun logCheckingAndUpdatingSubscriptionStatus(
        currentState: CurrentState,
        isNotificationEnableForApplication: Boolean
    ) {
        if (LoggingBehavior.MESSAGING in loggingBehavior) {
            Log.d(LOG_TAG, "CHECKING AND UPDATING SUBSCRIPTION STATUS \nWITH CURRENT STATE: $currentState \nWITH NOTIFICATION STATUS FOR APPLICATION: ${if (isNotificationEnableForApplication) "ENABLED" else "DISABLED"}")
        }
    }

    fun logSettingNewToken(
        token: String
    ) {
        if (LoggingBehavior.MESSAGING in loggingBehavior) {
            Log.d(LOG_TAG, "SETTING NEW PUSH TOKEN: $token")
        }
    }

    fun logChangingSubscriptionStatus(
        subscriptionStatus: MessagingPushSubscriptionStatus,
        newPushToken: String?,
        setBySDK: Boolean,
        state: CurrentState
    ) {
        if (LoggingBehavior.MESSAGING in loggingBehavior) {
            var message = "CHANGING SUBSCRIPTION STATUS TO: ${subscriptionStatus.name}"

            if (newPushToken != null) {
                message += "\nWITH NEW PUSH TOKEN: $newPushToken"
            }

            message += if (setBySDK) {
                "\nCAUSED BY: SDK"
            } else {
                "\nCAUSED BY: APPLICATION"
            }

            message += "\nWITH CURRENT STATE: $state"

            Log.d(LOG_TAG, message)
        }
    }

    fun logDeviceActivity(activity: BaseActivity) {
        if (LoggingBehavior.DEVICE_ACTIVITY in loggingBehavior) {
            Log.d(LOG_TAG, "SEND RECOMMEND DEVICE ACTIVITY\nTYPE: ${activity.getType()}\nACTIVITY MODEL: $activity")
        }
    }

    fun logException(e: Throwable) {
        Log.d(LOG_TAG, "EXCEPTION in RECOMMEND SDK: $e")
    }

    fun logRawRequest(requestTask: RequestTask<*>) {
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

        val serializedRequest = ApiTask.RepeatableApiTask.SerializedRequest(
            requestTask.call.request().url().toString(),
            requestTask.call.request().method(),
            serializedHeaders,
            serializedBody
        )

        if (LoggingBehavior.API in loggingBehavior) {
            var log = "SEND ${serializedRequest.method} RECOMMEND REQUEST to ${serializedRequest.url} with:\nHEADERS: ${serializedRequest.headers}"
            if (serializedRequest.body.isNotEmpty()) {
                log += "\nBODY: ${serializedRequest.body}"
            }
            Log.d(LOG_TAG, log)
        }
    }

    fun logRawResponse(rawResponse: String) {
        if (LoggingBehavior.API in loggingBehavior) {
            Log.d(LOG_TAG, "GET RAW RECOMMEND RESPONSE: $rawResponse")
        }
    }

    fun logResponse(responseModel: Any) {
        if (LoggingBehavior.API in loggingBehavior) {
            Log.d(LOG_TAG, "GET RECOMMEND RESPONSE MODEL $responseModel")
        }
    }

    fun logRequestException(e: Throwable) {
        if (LoggingBehavior.API in loggingBehavior) {
            Log.d(LOG_TAG, "GET RECOMMEND REQUEST ERROR $e")
        }
    }
}
