package com.recommend.sdk.device.data.model.activity

/**
 * Login activity. Send when customer logged in.
 *
 * @constructor Create Login activity
 */
class DeviceLoginActivity: BaseActivity {
    companion object {
        const val TYPE = "login"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return null
    }
}
