package com.recommend.sdk.messaging.data.model

import com.google.gson.annotations.SerializedName

/**
 * Messaging push subscription status
 */
enum class MessagingPushSubscriptionStatus {
    @SerializedName("subscribed")
    SUBSCRIBED,
    @SerializedName("unsubscribed")
    UNSUBSCRIBED,
    @SerializedName("non_subscribed")
    NON_SUBSCRIBED
}
