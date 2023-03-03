package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceCheckoutActivityData

/**
 * Checkout activity. Send this on checkout opening.
 *
 * @param step
 * @param option
 * @constructor Create Checkout activity
 */
class DeviceCheckoutActivity(
    step: String,
    option: String
): BaseActivity {
    private val data = DeviceCheckoutActivityData(
        step,
        option
    )

    companion object {
        const val TYPE = "checkout"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
