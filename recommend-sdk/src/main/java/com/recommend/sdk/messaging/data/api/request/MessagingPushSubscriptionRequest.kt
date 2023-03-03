package com.recommend.sdk.messaging.data.api.request

import com.google.gson.annotations.SerializedName
import com.recommend.sdk.messaging.data.model.MessagingPushSubscriptionStatus
import com.recommend.sdk.messaging.data.model.MessagingPushToken

/**
 * Messaging push subscription request
 *
 * @property token
 * @property applicationId
 * @property subscriptionStatus
 * @property subscriptionStatusChangeDate
 * @property firstSubscribedDate
 * @constructor Create Messaging push subscription request
 */
data class MessagingPushSubscriptionRequest(
    val token: MessagingPushToken,
    @SerializedName("application_id") val applicationId: String,
    @SerializedName("subscription_status") val subscriptionStatus: MessagingPushSubscriptionStatus? = null,
    @SerializedName("subscription_status_change_date") val subscriptionStatusChangeDate: Int? = null,
    @SerializedName("first_subscribed_date") val firstSubscribedDate: Int? = null
)
