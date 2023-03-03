package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceCustomerRegistrationActivityData

/**
 * Customer registration activity. Send after customer registration.
 *
 * @param requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Customer registration activity
 */
class DeviceCustomerRegistrationActivity(
    requestId: String? = null
): BaseActivity {
    private val data = DeviceCustomerRegistrationActivityData(requestId)

    companion object {
        const val TYPE = "customer_registration"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
