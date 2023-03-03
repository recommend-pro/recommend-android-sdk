package com.recommend.sdk.device.data.model.activity.data

/**
 * Device time
 *
 * @property timezone
 * @property date
 * @constructor Create Device time
 */
data class DeviceTime(
    val timezone: DeviceTimeZone,
    val date: String
) {
    data class DeviceTimeZone(
        val code: String,
        val offset: Int
    )
}
