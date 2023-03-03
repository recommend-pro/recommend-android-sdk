package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Remove from cart event data
 *
 * @property cartHash Hashed cart identifier SHA256
 * @property sku Master product identifier
 * @property variationSku Simple (size) product identifier
 * @property requestId Backend API request identifier linked with activity. Recommend: request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts)
 * @constructor Create empty Remove from cart event data
 */
data class DeviceRemoveFromCartActivityData(
    @SerializedName("cart_hash") val cartHash: String,
    val sku: String,
    @SerializedName("variation_sku") val variationSku: String,
    @SerializedName("request_id") val requestId: String? = null
)
