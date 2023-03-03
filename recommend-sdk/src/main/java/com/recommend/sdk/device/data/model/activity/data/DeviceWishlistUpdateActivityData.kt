package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Wishlist update event data
 *
 * @property wishlistHash Hashed wishlist identifier SHA256
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Wishlist update event data
 */
data class DeviceWishlistUpdateActivityData(
    @SerializedName("wishlist_hash") val wishlistHash: String,
    @SerializedName("request_id") val requestId: String? = null
)
