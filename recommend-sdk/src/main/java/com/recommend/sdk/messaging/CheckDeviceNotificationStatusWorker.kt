package com.recommend.sdk.messaging

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.recommend.sdk.Recommend
import com.recommend.sdk.core.exception.RecommendNotInitException
import com.recommend.sdk.core.util.JsonHelper
import com.recommend.sdk.core.util.RecommendLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CheckDeviceNotificationStatusWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
): CoroutineWorker(appContext, workerParams) {
    companion object {
        const val CONFIG_PARAM_NAME = "CONFIG"
        const val CHECK_DEVICE_NOTIFICATION_STATUS_WORK_TAG = "CHECK_DEVICE_NOTIFICATION_STATUS_WORK"
    }

    override suspend fun doWork(): Result {
        val config = JsonHelper.fromJson(inputData.getString(CONFIG_PARAM_NAME), Recommend.Config::class.java)
        val recommendMessaging = try {
            Recommend.getMessagingService()
        } catch (e: RecommendNotInitException) {
            Recommend.init(
                appContext as Application,
                config.accountId,
                config.applicationId
            )
            Recommend.getMessagingService()
        }

        val logger = Recommend.getLogger()

        logger.logCheckDeviceNotificationStatusWorkerProgress("CheckDeviceNotificationStatusWorker WORK START")

        return suspendCoroutine { continuation ->
            try {
                recommendMessaging.checkAndUpdateSubscriptionStatus(
                    {
                        logger.logCheckDeviceNotificationStatusWorkerProgress("CheckDeviceNotificationStatusWorker WORK FINISH SUCCESSFULLY")
                        continuation.resume(Result.success())
                    },
                    {
                        logger.logCheckDeviceNotificationStatusWorkerProgress("CheckDeviceNotificationStatusWorker WORK FINISH WITH ERROR $it with message ${it.message}")
                        continuation.resume(Result.success())
                    }
                )
            } catch (e: Throwable) {
                logger.logCheckDeviceNotificationStatusWorkerProgress("CheckDeviceNotificationStatusWorker WORK FINISH WITH ERROR $e with message ${e.message}")
                continuation.resume(Result.success())
            }
        }
    }
}
