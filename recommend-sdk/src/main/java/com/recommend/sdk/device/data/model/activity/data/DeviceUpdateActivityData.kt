package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Update device event data
 *
 * @property model Device model identifier
 * @property name Device name (ex. "Username device")
 * @property firstLaunch First opening date. UTC timestamp.
 * @property systemName Operation system name
 * @property systemVersion Operation system version
 * @property appVersion Application version
 * @property deviceLanguage Operation system language (which customer uses as device language)
 * @property deviceCountry Device country (OS locale)
 * @property location Customer (device) geo-location
 * @constructor Create Update device event data
 */
data class DeviceUpdateActivityData(
    val model: String,
    val name: String,
    @SerializedName("first_launch") val firstLaunch: Int,
    @SerializedName("system_version") val systemVersion: String,
    @SerializedName("app_version") val appVersion: String,
    @SerializedName("device_language") val deviceLanguage: String,
    @SerializedName("device_country") val deviceCountry: String,
    val location: DeviceLocation,
    @SerializedName("system_name") val systemName: String = "android"
) {
}
