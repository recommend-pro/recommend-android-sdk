package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceRemoveFromCartActivityData

/**
 * Remove from cart activity. Send after product is removed from cart.
 *
 * @property data Remove from cart activity data
 * @constructor Create empty Remove from cart activity
 */
class DeviceRemoveFromCartActivity(
    cartHash: String,
    sku: String,
    variationSku: String,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceRemoveFromCartActivityData(
        cartHash,
        sku,
        variationSku,
        requestId
    )

    companion object {
        const val TYPE = "remove_from_cart"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return data
    }
}
