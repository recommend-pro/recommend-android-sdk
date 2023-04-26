package com.recommend.sdk.device

import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.recommend.sdk.Recommend
import com.recommend.sdk.core.data.ApiTask
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.api.ApiServiceBuilder
import com.recommend.sdk.core.data.model.Environment
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.DateTimeHelper
import com.recommend.sdk.device.data.api.ApiDeviceService
import com.recommend.sdk.device.data.api.request.DeviceActivityRequest
import com.recommend.sdk.device.data.api.response.DeviceActivityErrorResponse
import com.recommend.sdk.device.data.model.activity.*
import com.recommend.sdk.device.data.model.activity.data.*
import com.recommend.sdk.device.util.RecommendDeviceHelper
import kotlinx.coroutines.*
import java.util.*

/**
 * Recommend device
 *
 * @property recommend
 * @constructor Create Recommend device
 */
class RecommendDevice(
    private val recommend: Recommend
    ) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var isFirstActivityOpeningTracked = false
    private var needTrackOpenApp: Boolean = false
    private var isAutomaticTrackingEnabled: Boolean = true

    private val apiContactService: ApiDeviceService = ApiServiceBuilder.getService(
        recommend.config.accountId,
        recommend.config.apiHost,
        ApiDeviceService::class.java,
        recommend.getLogger()
    )

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object: DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                if (isAutomaticTrackingEnabled) {
                    if (!isFirstActivityOpeningTracked) {
                        isFirstActivityOpeningTracked = true
                        trackUpdateDevice()
                        trackOpenApp()
                    }
                    if (needTrackOpenApp) {
                        needTrackOpenApp = false
                        trackOpenApp()
                    }
                }

                super.onStart(owner)
            }

            override fun onStop(owner: LifecycleOwner) {
                if (isAutomaticTrackingEnabled) {
                    needTrackOpenApp = true
                }

                super.onStop(owner)
            }
        })
    }

    /**
     * Enable automatic tracking of UpdateDeviceActivity and OpenAppActivity
     */
    fun enableAutoEventTracking() {
        isAutomaticTrackingEnabled = true
    }

    /**
     * Disable automatic tracking of UpdateDeviceActivity and OpenAppActivity
     */
    fun disableAutoEventTracking() {
        isAutomaticTrackingEnabled = false
    }

    /**
     * Track open app activity. This activity is tracked automatically. This activity should be tracked on application launch and on application comes to foreground.
     *
     * @param metrics
     */
    private fun trackOpenApp(
        metrics: Metrics? = null
    ) {
        trackActivity(
            DeviceOpenAppActivity(),
            metrics
        )
    }

    /**
     * Track update device activity. This activity is tracked automatically. This event should be tracked on application start with device data or when device data changes.
     *
     * @param metrics
     */
    private fun trackUpdateDevice(
        metrics: Metrics? = null
    ) {
        scope.launch {
            val currentState = recommend.currentStateManager.getCurrentState()
            val isFirstLaunch = currentState.isFirstLaunch
            if (isFirstLaunch) {
                recommend.currentStateManager.onFirstLaunch()
            }

            val locale = Locale.getDefault()
            val country = RecommendDeviceHelper.getDetectedCountry(recommend.context, "")

            trackActivity(
                DeviceUpdateDeviceActivity(
                    Build.MODEL,
                    RecommendDeviceHelper.getDeviceName(recommend.context),
                    if (isFirstLaunch) 1 else 0,
                    Build.VERSION.RELEASE,
                    RecommendDeviceHelper.getCurrentApplicationVersionName(recommend.context),
                    locale.language,
                    country,
                    //TODO get location
                    DeviceLocation(0f, 0f)
                ),
                metrics
            )
        }
    }

    /**
     * Track device activity
     *
     * @param activity Activity
     * @param metrics Metrics
     */
    fun trackActivity(
        activity: BaseActivity,
        metrics: Metrics? = null
    ) {
        trackActivities(
            recommend.config.environment,
            listOf(activity),
            metrics
        )
    }

    private fun trackActivities(
        activityEnvironment: Environment,
        activities: List<BaseActivity>,
        metrics: Metrics? = null
    ) {
        val activityRequests = mutableListOf<DeviceActivityRequest.ActivityRequest>()
        activities.forEach {
            recommend.getLogger().logDeviceActivity(it)
            activityRequests.add(
                DeviceActivityRequest.ActivityRequest(
                    it.getType(),
                    it.getData()
                )
            )
        }

        val defaultMetrics = recommend.config.defaultMetrics
        val finalMetrics = if (metrics != null && defaultMetrics != null) {
            RecommendDeviceHelper.mergeMetrics(metrics, defaultMetrics)
        } else {
            metrics ?: defaultMetrics
        }

        scope.launch {
            recommend.getApiManager().processTask(
                ApiTask(
                    RequestTask(
                        apiContactService.trackActivity(
                            recommend.currentStateManager.getCurrentState().deviceId,
                            DeviceActivityRequest(
                                activityEnvironment.customerIdHash,
                                activityEnvironment.store,
                                activityEnvironment.currency,
                                activityEnvironment.environment,
                                activityEnvironment.priceList?.code,
                                DateTimeHelper.getDeviceTime(),
                                DateTimeHelper.getCurrentTime(),
                                finalMetrics,
                                activityRequests
                            )
                        )
                    ),
                    apiErrorResponseType = DeviceActivityErrorResponse::class.java
                )
            )
        }
    }
}
