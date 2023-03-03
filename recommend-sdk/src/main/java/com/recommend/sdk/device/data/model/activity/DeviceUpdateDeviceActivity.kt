package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceLocation
import com.recommend.sdk.device.data.model.activity.data.DeviceUpdateActivityData

/**
 * Update device information event. This event should be tracked on application start with device data or when device data changes.
 *
 * @param model Device model identifier
 * @param name Device name (ex. "Username device")
 * @param firstLaunch First opening date. UTC timestamp.
 * @param systemName Operation system name
 * @param systemVersion Operation system version
 * @param appVersion Application version
 * @param deviceLanguage Operation system language (which customer uses as device language)
 * @param deviceCountry Device country (OS locale)
 * @param location Customer (device) geo-location
 * @constructor Create Update device activity
 */
class DeviceUpdateDeviceActivity(
    model: String,
    name: String,
    firstLaunch: Int,
    systemVersion: String,
    appVersion: String,
    deviceLanguage: String,
    deviceCountry: String,
    location: DeviceLocation,
    systemName: String = "android"
): BaseActivity {
    private val data = DeviceUpdateActivityData(
        model,
        name,
        firstLaunch,
        systemVersion,
        appVersion,
        deviceLanguage,
        deviceCountry,
        location,
        systemName
    )

    companion object {
        const val TYPE = "update_device"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
