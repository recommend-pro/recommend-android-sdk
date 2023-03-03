package com.recommend.sdk.device.data.model.activity

/**
 * Logout activity. Send when customer logged out.
 *
 * @constructor Create empty Logout activity
 */
class DeviceLogoutActivity: BaseActivity {
    companion object {
        const val TYPE = "logout"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return null
    }
}
