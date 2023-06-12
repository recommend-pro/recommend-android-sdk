package com.recommend.sdk.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.recommend.sdk.Recommend
import com.recommend.sdk.core.data.ApiTask
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.api.ApiServiceBuilder
import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.exception.RecommendException
import com.recommend.sdk.core.util.DateTimeHelper
import com.recommend.sdk.core.util.JsonHelper
import com.recommend.sdk.messaging.data.api.ApiMessagingService
import com.recommend.sdk.messaging.data.api.request.MessagingPushEventRequest
import com.recommend.sdk.messaging.data.api.request.MessagingPushSubscriptionRequest
import com.recommend.sdk.messaging.data.api.response.MessagingPushEventErrorResponse
import com.recommend.sdk.messaging.data.api.response.MessagingPushSubscriptionErrorResponse
import com.recommend.sdk.messaging.data.model.MessagingPushSubscriptionStatus
import com.recommend.sdk.messaging.data.model.MessagingPushToken
import com.recommend.sdk.messaging.exception.RecommendMessagingException
import com.recommend.sdk.messaging.push.RecommendPush
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Recommend messaging
 *
 * @property recommend
 * @constructor Create empty Recommend messaging
 */
class RecommendMessaging(
    private val recommend: Recommend
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var smallIconDrawable: Int? = null

    /**
     * On receive push notification listener. Return True if the listener has consumed the push, false otherwise.
     */
    private var onReceivePushNotificationListener: ((recommendPush: RecommendPush) -> Boolean)? = null

    private val apiMessagingService: ApiMessagingService = ApiServiceBuilder.getService(
        recommend.config.accountId,
        recommend.config.apiHost,
        ApiMessagingService::class.java,
        recommend.getLogger()
    )

    companion object {
        const val NOTIFICATION_DATA_IMAGE = "image-url"
        const val NOTIFICATION_DATA_URL = "open-url"
        const val NOTIFICATION_DATA_ID = "__rec__mid"
        const val NOTIFICATION_DATA_TITLE = "title"
        const val NOTIFICATION_DATA_BODY = "body"
        const val NOTIFICATION_CHANNEL_ID = "recommend_channel_id_01"
        const val RECOMMEND_PUSH_PAYLOAD = "RECOMMEND_PUSH_PAYLOAD"
    }

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresBatteryNotLow(true)
            .build()
        val uploadWorkRequest = PeriodicWorkRequestBuilder<CheckDeviceNotificationStatusWorker>(1, TimeUnit.HOURS)
                .setInputData(
                    workDataOf(
                        CheckDeviceNotificationStatusWorker.CONFIG_PARAM_NAME to JsonHelper.toJson(recommend.config)
                    )
                )
                .setInitialDelay(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        WorkManager
            .getInstance(recommend.context)
            .enqueueUniquePeriodicWork(
                CheckDeviceNotificationStatusWorker.CHECK_DEVICE_NOTIFICATION_STATUS_WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                uploadWorkRequest
            )
    }

    fun setSmallIconDrawable(smallIconDrawable: Int) {
        this.smallIconDrawable = smallIconDrawable
    }

    fun setOnReceivePushNotificationListener(onReceivePushNotificationListener: (recommendPush: RecommendPush) -> Boolean) {
        this.onReceivePushNotificationListener = onReceivePushNotificationListener
    }

    fun removeOnReceivePushNotificationListener() {
        this.onReceivePushNotificationListener = null
    }

    fun processMessage(messageData: Map<String, String>) {
        if (isRecommendMessage(messageData)) {
            val recommendId = messageData[NOTIFICATION_DATA_ID]!!
            val title = messageData.getOrDefault(NOTIFICATION_DATA_TITLE, null)
            val body = messageData.getOrDefault(NOTIFICATION_DATA_BODY, null)
            val url = messageData.getOrDefault(NOTIFICATION_DATA_URL, null)
            val image = messageData.getOrDefault(NOTIFICATION_DATA_IMAGE, null)
            val recommendPush = RecommendPush(
                recommendId,
                title,
                body,
                messageData,
                url,
                image
            )

            trackPushNotificationsEvent(
                recommendPush,
                isDelivered = true
            )

            val onReceivePushNotificationListener = onReceivePushNotificationListener

            if (onReceivePushNotificationListener != null && onReceivePushNotificationListener(recommendPush)) {
                return
            }

            if (areNotificationsEnabled()) {
                val notificationManager = recommend.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "Recommend Notifications",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationChannel.description = "Recommend notifications channel"
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.vibrationPattern = longArrayOf(0, 250, 250)
                    notificationChannel.enableVibration(true)
                    notificationManager.createNotificationChannel(notificationChannel)
                }

                val builder: NotificationCompat.Builder = NotificationCompat.Builder(recommend.context, NOTIFICATION_CHANNEL_ID)

                val time = System.currentTimeMillis()
                val notificationId = (time / 1000).toInt()

                if (recommendPush.url != null) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recommendPush.url))
                    intent.putExtra(RECOMMEND_PUSH_PAYLOAD, JsonHelper.toJson(recommendPush))

                    val pendingIntent = PendingIntent.getActivity(
                        recommend.context,
                        notificationId,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    builder.setContentIntent(pendingIntent)
                }

                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                if(smallIconDrawable != null) {
                    builder.setSmallIcon(smallIconDrawable!!)
                } else {
                    builder.setSmallIcon(recommend.context.applicationInfo.icon)
                }

                builder.setContentTitle(recommendPush.title)
                builder.setContentText(recommendPush.body)
                builder.setSound(uri)
                builder.setVibrate(longArrayOf(0, 250, 250))
                builder.setAutoCancel(true)

                if (recommendPush.image != null) {
                    try {
                        val bigPictureStyle = NotificationCompat.BigPictureStyle()
                        bigPictureStyle.bigPicture(getImage(recommendPush.image))

                        if (recommendPush.title != null) {
                            bigPictureStyle.setBigContentTitle(recommendPush.title)
                        }

                        if (recommendPush.body != null) {
                            bigPictureStyle.setSummaryText(recommendPush.body)
                        }

                        builder.setStyle(bigPictureStyle)
                        builder.setLargeIcon(getImage(recommendPush.image))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                val notification = builder.build()
                notificationManager.notify(notificationId, notification)
            } else {
                checkAndUpdateSubscriptionStatus()
            }
        }
    }

    private fun getImage(image: String?): Bitmap? {
        try {
            val url = URL(image)
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun isRecommendMessage(messageData: Map<String, String>): Boolean {
        return NOTIFICATION_DATA_ID in messageData.keys
                && (NOTIFICATION_DATA_TITLE in messageData.keys
                || NOTIFICATION_DATA_BODY in messageData.keys)
    }

    fun setPushToken(
        token: String,
        onComplete: (() -> Unit)? = null,
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        if (!areNotificationsEnabled()) {
            onError?.let { it(RecommendMessagingException(RecommendMessagingException.ErrorCode.NOTIFICATION_IS_DISABLED)) }
        }

        scope.launch {
            val currentState = recommend.currentStateManager.getCurrentState()
            currentState.pushToken = token
            recommend.currentStateManager.saveCurrentState(currentState)

            val pushSubscriptionStatus = if (currentState.isSubscribedToPush == null) {
                MessagingPushSubscriptionStatus.NON_SUBSCRIBED
            } else if (currentState.isSubscribedToPush!!) {
                MessagingPushSubscriptionStatus.SUBSCRIBED
            } else {
                MessagingPushSubscriptionStatus.UNSUBSCRIBED
            }

            recommend.getLogger().logSettingNewToken(token)

            sendSuscriptionStatus(
                pushSubscriptionStatus,
                token,
                setBySDK = true,
                onComplete = onComplete,
                onError = onError
            )
        }
    }

    fun getSubscriptionStatus(
        onComplete: ((status: MessagingPushSubscriptionStatus) -> Unit),
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError?.let { it(exception) }
        }

        scope.launch(exceptionHandler) {
            val currentState = recommend.currentStateManager.getCurrentState()
            val status = when(currentState.isSubscribedToPush) {
                true -> MessagingPushSubscriptionStatus.SUBSCRIBED
                false -> MessagingPushSubscriptionStatus.UNSUBSCRIBED
                null -> MessagingPushSubscriptionStatus.NON_SUBSCRIBED
            }
            onComplete(status)
        }
    }

    fun setSubscriptionStatus(
        subscriptionStatus: MessagingPushSubscriptionStatus,
        newPushToken: String? = null,
        onComplete: (() -> Unit)? = null,
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        sendSuscriptionStatus(
            subscriptionStatus = subscriptionStatus,
            newPushToken = newPushToken,
            setBySDK = false,
            onComplete = onComplete,
            onError = onError)
    }

    private fun sendSuscriptionStatus(
        subscriptionStatus: MessagingPushSubscriptionStatus,
        newPushToken: String? = null,
        setBySDK: Boolean = false,
        onComplete: (() -> Unit)? = null,
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        scope.launch {
            val currentState = recommend.currentStateManager.getCurrentState()
            val currentTime = DateTimeHelper.getCurrentTime().toInt()

            recommend.getLogger().logChangingSubscriptionStatus(
                subscriptionStatus,
                newPushToken,
                setBySDK,
                currentState
            )

            val pushToken = newPushToken ?: currentState.pushToken
            if (pushToken == null) {
                onError?.let { it(RecommendMessagingException(RecommendMessagingException.ErrorCode.PUSH_TOKEN_NOT_SET)) }
                return@launch
            }
            if (newPushToken != null && newPushToken != currentState.pushToken) {
               currentState.pushToken = newPushToken
            }

            val firstSubscribedDate = if (currentState.firstSubscribedDate == null) {
                if (subscriptionStatus == MessagingPushSubscriptionStatus.SUBSCRIBED) {
                    currentState.firstSubscribedDate = currentTime
                    currentState.firstSubscribedDate
                } else {
                    null
                }
            } else {
                currentState.firstSubscribedDate
            }
            currentState.subscriptionStatusChangeDate = currentTime

            recommend.currentStateManager.saveCurrentState(currentState)

            recommend.getApiManager().processTask(
                ApiTask(
                    RequestTask(
                        apiMessagingService.updatePushNotificationsSubscription(
                            recommend.currentStateManager.getCurrentState().deviceId,
                            MessagingPushSubscriptionRequest(
                                MessagingPushToken(
                                    pushToken
                                ),
                                recommend.config.applicationId,
                                subscriptionStatus,
                                currentState.subscriptionStatusChangeDate,
                                firstSubscribedDate
                            )
                        ),
                        dataListener = DataListener(
                            { response, _ ->
                                if (response != null) {
                                    scope.launch {
                                        when(subscriptionStatus) {
                                            MessagingPushSubscriptionStatus.SUBSCRIBED -> {
                                                currentState.isSubscribedToPush = true
                                                if (!setBySDK) {
                                                    currentState.lastSentManuallySubscribedToPushStatus = true
                                                }
                                            }
                                            MessagingPushSubscriptionStatus.UNSUBSCRIBED -> {
                                                currentState.isSubscribedToPush = false
                                                if (!setBySDK) {
                                                    currentState.lastSentManuallySubscribedToPushStatus = false
                                                }
                                            }
                                            MessagingPushSubscriptionStatus.NON_SUBSCRIBED -> {
                                                currentState.isSubscribedToPush = null
                                                if (!setBySDK) {
                                                    currentState.lastSentManuallySubscribedToPushStatus = null
                                                }
                                            }
                                        }

                                        recommend.currentStateManager.saveCurrentState(currentState)
                                        onComplete?.let { it() }
                                    }
                                } else {
                                    onError?.let { it(RecommendException("Response is empty")) }
                                }
                            },
                            { error, _ ->
                                onError?.let { it(error) }
                            }
                        )
                    ),
                    apiErrorResponseType = MessagingPushSubscriptionErrorResponse::class.java
                )
            )
        }
    }

    internal fun checkAndUpdateSubscriptionStatus(
        onComplete: (() -> Unit)? = null,
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        scope.launch {
            val currentState = recommend.currentStateManager.getCurrentState()

            val areNotificationsEnabled = areNotificationsEnabled()

            recommend.getLogger().logCheckingAndUpdatingSubscriptionStatus(
                currentState,
                areNotificationsEnabled
            )

            if (currentState.lastSentManuallySubscribedToPushStatus == true && currentState.isSubscribedToPush == true && !areNotificationsEnabled) {
                sendSuscriptionStatus(
                    MessagingPushSubscriptionStatus.UNSUBSCRIBED,
                    setBySDK = true,
                    onComplete = onComplete,
                    onError = onError
                )
            } else if (areNotificationsEnabled && currentState.lastSentManuallySubscribedToPushStatus == true && currentState.isSubscribedToPush == false) {
                sendSuscriptionStatus(
                    MessagingPushSubscriptionStatus.SUBSCRIBED,
                    setBySDK = true,
                    onComplete = onComplete,
                    onError = onError
                )
            } else {
                onComplete?.let { it() }
            }
        }
    }

    private fun areNotificationsEnabled(): Boolean = NotificationManagerCompat.from(recommend.context).areNotificationsEnabled()

    fun onReceiveIntent(intent: Intent) {
        if (intent.hasExtra(RECOMMEND_PUSH_PAYLOAD)) {
            try {
                val recommendPush = JsonHelper.fromJson(
                    intent.getStringExtra(RECOMMEND_PUSH_PAYLOAD),
                    RecommendPush::class.java
                )

                trackPushNotificationsEvent(
                    recommendPush,
                    isDelivered = true,
                    isClicked = true,
                    openURL = recommendPush.url
                )
            } catch (e: Throwable) {
                recommend.getLogger().logException(e)
            }
        }
    }

    fun trackPushNotificationsEvent(
        recommendPush: RecommendPush,
        isDelivered: Boolean? = null,
        isClicked: Boolean? = null,
        openURL: String? = null,
        onComplete: (() -> Unit)? = null,
        onError: ((error: Throwable) -> Unit)? = null
    ) {
        val currentTime = DateTimeHelper.getCurrentTime().toInt()
        recommend.getApiManager().processTask(
            ApiTask(
                RequestTask(
                    apiMessagingService.trackPushNotificationsEvent(
                        recommendPush.id,
                        MessagingPushEventRequest(
                            received = isDelivered,
                            clicked = isClicked,
                            openURL = openURL,
                            eventTime = currentTime
                        )
                    ),
                    dataListener = DataListener(
                        { response, _ ->
                            if (response != null) {
                                onComplete?.let { it() }
                            } else {
                                onError?.let { it(RecommendException("Response is empty")) }
                            }
                        },
                        { error, _ ->
                            onError?.let { it(error) }
                        }
                    )
                ),
                repeatConfig = ApiTask.RepeatConfig(
                    needRepeatWhenNoInternetConnection = true,
                    needRepeatOnUnsuccess = true,
                    repeatPattern = listOf(
                        Pair(5, TimeUnit.SECONDS),
                        Pair(30, TimeUnit.SECONDS),
                        Pair(1, TimeUnit.MINUTES),
                        Pair(5, TimeUnit.MINUTES),
                        Pair(30, TimeUnit.MINUTES),
                    )
                ),
                apiErrorResponseType = MessagingPushEventErrorResponse::class.java
            )
        )
    }
}
