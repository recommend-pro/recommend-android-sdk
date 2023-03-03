package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Subscribe event data
 *
 * @property emailHash Hashed email SHA256
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Subscribe event data
 */
data class DeviceSubscribeActivityData(
    @SerializedName("email_hash") val emailHash: String,
    @SerializedName("request_id") val requestId: String? = null
)
