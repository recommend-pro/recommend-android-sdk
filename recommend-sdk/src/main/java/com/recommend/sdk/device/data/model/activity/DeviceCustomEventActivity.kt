package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceCustomEventActivityData

/**
 * Custom event. Custom event on device for trigger action in Smart Campaign.
 * One-time event for interacting with Smart Campaign. Possible to trigger an action and do something based on it in Smart Campaign.
 *
 * @param event Event name
 * @constructor Create Custom event activity
 */
class DeviceCustomEventActivity(
    event: String
): BaseActivity {
    private val data = DeviceCustomEventActivityData(
        event
    )

    companion object {
        const val TYPE = "custom_event"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
