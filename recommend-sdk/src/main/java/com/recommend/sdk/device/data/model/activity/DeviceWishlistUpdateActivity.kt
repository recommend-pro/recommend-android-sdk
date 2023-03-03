package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceWishlistUpdateActivityData

/**
 * Wishlist update activity.
 *
 * @param wishlistHash Hashed wishlist identifier SHA256
 * @param requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Wishlist update activity
 */
class DeviceWishlistUpdateActivity(
    wishlistHash: String,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceWishlistUpdateActivityData(
            wishlistHash,
        requestId
    )

    companion object {
        const val TYPE = "wishlist_update"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
