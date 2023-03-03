package com.recommend.sdk.messaging.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Messaging push event error response
 *
 * @property success
 * @property errorCode
 * @property errorMessage
 * @constructor Create Messaging push event error response
 */
data class MessagingPushEventErrorResponse(
    val success: Boolean,
    @SerializedName("error_code") val errorCode: String?,
    @SerializedName("error_message") val errorMessage: String?
)
