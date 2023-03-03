package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceRemoveFromWishlistActivityData

/**
 * Remove from wishlist activity. Send after product is removed from wishlist.
 *
 * @property data Remove from wishlist activity data
 * @constructor Create Remove from wishlist activity
 */
class DeviceRemoveFromWishlistActivity(
    wishlistHash: String,
    sku: String,
    variationSku: String?,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceRemoveFromWishlistActivityData(
        wishlistHash,
        sku,
        variationSku,
        requestId
    )

    companion object {
        const val TYPE = "remove_from_wishlist"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
