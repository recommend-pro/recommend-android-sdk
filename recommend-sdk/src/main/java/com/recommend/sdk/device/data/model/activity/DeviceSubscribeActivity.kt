package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceSubscribeActivityData

/**
 * Subscribe activity. Send after email subscription.
 *
 * @param emailHash Hashed email SHA256
 * @param requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Subscribe activity
 */
class DeviceSubscribeActivity(
    emailHash: String,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceSubscribeActivityData(
        emailHash,
        requestId
    )

    companion object {
        const val TYPE = "subscribe"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
