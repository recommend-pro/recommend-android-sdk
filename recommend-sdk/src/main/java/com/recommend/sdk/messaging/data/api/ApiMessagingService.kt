package com.recommend.sdk.messaging.data.api

import com.recommend.sdk.core.data.api.UniqueIdTag
import com.recommend.sdk.messaging.data.api.request.MessagingPushEventRequest
import com.recommend.sdk.messaging.data.api.request.MessagingPushSubscriptionRequest
import com.recommend.sdk.messaging.data.api.response.MessagingPushEventResponse
import com.recommend.sdk.messaging.data.api.response.MessagingPushSubscriptionResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Api messaging service
 */
interface ApiMessagingService {
    @POST("messaging/channel/push/android/subscription/{device_id}")
    fun updatePushNotificationsSubscription(
        @Path("device_id") deviceId: String,
        @Body body: MessagingPushSubscriptionRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Tag uniqueIdTag: UniqueIdTag = UniqueIdTag()
    ): Call<MessagingPushSubscriptionResponse>

    @POST("messaging/channel/push/{push_id}/event")
    fun trackPushNotificationsEvent(
        @Path("push_id") pushId: String,
        @Body body: MessagingPushEventRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Tag uniqueIdTag: UniqueIdTag = UniqueIdTag()
    ): Call<MessagingPushEventResponse>
}
