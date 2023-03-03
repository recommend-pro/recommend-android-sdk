package com.recommend.sdk.messaging.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Messaging push subscription error response
 *
 * @property success
 * @property errorCode
 * @property errorMessage
 * @constructor Create Messaging push subscription error response
 */
data class MessagingPushSubscriptionErrorResponse(
    val success: Boolean,
    @SerializedName("error_code") val errorCode: String?,
    @SerializedName("error_message") val errorMessage: String?
)
