package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Sale event data
 *
 * @property orderIdHash Hashed order identifier SHA256
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Sale event data
 */
data class DeviceSaleActivityData(
    @SerializedName("order_id_hash") val orderIdHash: String,
    @SerializedName("request_id") val requestId: String? = null
)
