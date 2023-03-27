package com.recommend.sdk

import android.content.Context
import com.recommend.sdk.core.CurrentStateManager
import com.recommend.sdk.core.data.ApiManager
import com.recommend.sdk.core.data.model.CurrentState
import com.recommend.sdk.core.data.model.Environment
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.exception.RecommendNotInitException
import com.recommend.sdk.core.util.LoggingBehavior
import com.recommend.sdk.core.util.RecommendLogger
import com.recommend.sdk.device.RecommendDevice
import com.recommend.sdk.messaging.RecommendMessaging
import com.recommend.sdk.recommendation.RecommendRecommendation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Recommend
 *
 * @property context Context context
 * @property config Recommend config
 * @constructor Create Recommend
 */
class Recommend private constructor(
    val context: Context,
    internal val config: Config
) {
    data class Config(
        val accountId: String,
        val applicationId: String,
        val apiHost: String,
        var environment: Environment = Environment(),
        var defaultMetrics: Metrics? = null,
    ) {
        companion object {
            const val DEFAULT_API_HOST = "https://api.recommend.pro/v3"
        }
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val recommendLogger = RecommendLogger()
    private val apiManager = ApiManager(context, recommendLogger, ApiHelper(context))
    internal val currentStateManager = CurrentStateManager(context)
    private val recommendMessaging = RecommendMessaging(this)
    private val recommendDevice =  RecommendDevice(this)
    private val recommendRecommendation = RecommendRecommendation(this)

    companion object {
        private lateinit var instance: Recommend

        fun init(
            context: Context,
            accountId: String,
            applicationId: String,
            apiHost: String = Config.DEFAULT_API_HOST
        ) {
            instance = Recommend(
                context,
                Config(
                    accountId,
                    applicationId,
                    apiHost
                )
            )
        }

        @Synchronized
        private fun getInstance(): Recommend {
            if (!this::instance.isInitialized) {
                throw RecommendNotInitException()
            }

            return instance
        }

        fun getDeviceService(): RecommendDevice {
            return getInstance().getDeviceService()
        }

        fun getRecommendationService(): RecommendRecommendation {
            return getInstance().getRecommendationService()
        }

        fun getMessagingService(): RecommendMessaging {
            return getInstance().getMessagingService()
        }

        fun addLoggingBehavior(behavior: LoggingBehavior) {
            getInstance().recommendLogger.addLoggingBehavior(behavior)
        }

        fun removeLoggingBehavior(behavior: LoggingBehavior) {
            getInstance().recommendLogger.removeLoggingBehavior(behavior)
        }

        fun setEnvironment(environment: Environment) {
            getInstance().config.environment = environment
        }

        fun setDefaultMetrics(metrics: Metrics) {
            getInstance().config.defaultMetrics = metrics
        }

        @Synchronized
        fun getDeviceId(onResult: (deviceId: String) -> Unit) {
            getInstance().getDeviceId(onResult)
        }

        @Synchronized
        fun setDeviceId(deviceId: String, onResult: (() -> Unit)? = null) {
            getInstance().setDeviceId(deviceId, onResult)
        }

        internal fun getLogger(): RecommendLogger {
            return getInstance().getLogger()
        }
    }

    internal fun getLogger(): RecommendLogger {
        return recommendLogger
    }

    internal fun getApiManager(): ApiManager {
        return apiManager
    }

    private fun getDeviceService(): RecommendDevice {
        return recommendDevice
    }

    private fun getRecommendationService(): RecommendRecommendation {
        return recommendRecommendation
    }

    private fun getMessagingService(): RecommendMessaging {
        return recommendMessaging
    }

    @Synchronized
    internal fun getDeviceId(onResult: (deviceId: String) -> Unit) {
        scope.launch {
            onResult(currentStateManager.getCurrentState().deviceId)
        }
    }

    @Synchronized
    internal fun setDeviceId(deviceId: String, onResult: (() -> Unit)? = null) {
        scope.launch {
            val currentState = currentStateManager.getCurrentState()
            currentState.deviceId = deviceId
            currentStateManager.saveCurrentState(currentState)
            onResult?.let { it() }
        }
    }

    internal fun getCurrentState(onResult: (currentState: CurrentState) -> Unit) {
        scope.launch {
            onResult(currentStateManager.getCurrentState())
        }
    }
}
