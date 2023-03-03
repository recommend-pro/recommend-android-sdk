package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Remove from wishlist event data
 *
 * @property wishlistHash Hashed wishlist identifier SHA256
 * @property sku Master product identifier
 * @property variationSku Simple (size) product identifier
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts)
 * @constructor Create Add to wishlist event data
 */
data class DeviceRemoveFromWishlistActivityData(
    @SerializedName("wishlist_hash") val wishlistHash: String,
    val sku: String,
    @SerializedName("variation_sku") val variationSku: String?,
    @SerializedName("request_id") val requestId: String? = null
)
