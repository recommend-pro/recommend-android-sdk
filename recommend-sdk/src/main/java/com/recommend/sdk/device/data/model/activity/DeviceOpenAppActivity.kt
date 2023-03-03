package com.recommend.sdk.device.data.model.activity

/**
 * Open app event. This event should be tracked on application launch and on application comes to foreground.
 *
 * @constructor Create empty Open app activity
 */
class DeviceOpenAppActivity: BaseActivity {
    companion object {
        const val TYPE = "open_app"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return null
    }
}
