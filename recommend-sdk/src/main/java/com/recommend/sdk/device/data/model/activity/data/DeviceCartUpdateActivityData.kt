package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Cart update event data
 *
 * @property cartHash Hashed cart identifier SHA256
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Cart update event data
 */
data class DeviceCartUpdateActivityData(
    @SerializedName("cart_hash") val cartHash: String,
    @SerializedName("request_id") val requestId: String? = null
)
