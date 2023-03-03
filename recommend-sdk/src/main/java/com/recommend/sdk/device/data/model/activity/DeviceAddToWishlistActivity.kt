package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceAddToWishlistActivityData

/**
 * Add to wishlist activity. Send after product is added to wishlist.
 *
 * @param wishlistHash Hashed wishlist identifier SHA256
 * @param sku Master product identifier
 * @param variationSku Simple (size) product identifier
 * @param requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts)
 * @constructor Create Add to wishlist activity
 */
class DeviceAddToWishlistActivity(
    wishlistHash: String,
    sku: String,
    variationSku: String?,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceAddToWishlistActivityData(
        wishlistHash,
        sku,
        variationSku,
        requestId
    )

    companion object {
        const val TYPE = "add_to_wishlist"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
