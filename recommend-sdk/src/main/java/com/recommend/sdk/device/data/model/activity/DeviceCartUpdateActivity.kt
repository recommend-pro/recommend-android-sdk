package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceCartUpdateActivityData

/**
 * Cart update activity
 *
 * @param cartHash Hashed cart identifier SHA256
 * @param requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create empty Cart update activity
 */
class DeviceCartUpdateActivity(
    cartHash: String,
    requestId: String? = null
): BaseActivity {
    private val data: DeviceCartUpdateActivityData = DeviceCartUpdateActivityData(
        cartHash,
        requestId
    )

    companion object {
        const val TYPE = "cart_update"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
