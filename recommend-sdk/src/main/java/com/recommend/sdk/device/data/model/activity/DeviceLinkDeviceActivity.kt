package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceLinkDeviceActivityData

/**
 * Link device activity
 *
 * @param deviceIdsToLink Device ids
 * @constructor Create Link device activity
 */
class DeviceLinkDeviceActivity(
    deviceIdsToLink: List<String>
): BaseActivity {
    private val data = DeviceLinkDeviceActivityData(deviceIdsToLink)

    companion object {
        const val TYPE = "link_device"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return data
    }
}
