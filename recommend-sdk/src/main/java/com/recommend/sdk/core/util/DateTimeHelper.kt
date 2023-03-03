package com.recommend.sdk.core.util

import com.recommend.sdk.device.data.model.activity.data.DeviceTime
import java.text.SimpleDateFormat
import java.util.*

class DateTimeHelper {
    companion object {
        fun getDeviceTime(): DeviceTime {
            val timeZone = TimeZone.getDefault()
            val now = Date()
            val secondsFromGMT = timeZone.getOffset(now.time) / 1000

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK)
            val formattedDate: String = sdf.format(Date())

            return DeviceTime(
                DeviceTime.DeviceTimeZone(
                    timeZone.getDisplayName(false, TimeZone.SHORT).substring(0, 3),
                    secondsFromGMT
                ),
                formattedDate
            )
        }

        fun getCurrentTime(): Long {
            return Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.time / 1000
        }
    }
}
