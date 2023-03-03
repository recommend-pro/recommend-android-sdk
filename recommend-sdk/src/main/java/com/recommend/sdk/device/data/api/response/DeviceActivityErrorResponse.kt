package com.recommend.sdk.device.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Device activity error response
 *
 * @property success
 * @property errorCode
 * @property errorMessage
 * @constructor Create Device activity error response
 */
data class DeviceActivityErrorResponse(
    val success: Boolean,
    @SerializedName("error_code") val errorCode: String,
    @SerializedName("error_message") val errorMessage: String
)
