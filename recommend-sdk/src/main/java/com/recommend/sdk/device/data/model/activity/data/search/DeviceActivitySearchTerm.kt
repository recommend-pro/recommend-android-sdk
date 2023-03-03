package com.recommend.sdk.device.data.model.activity.data.search

import com.google.gson.annotations.SerializedName

/**
 * DeviceActivitySearchTerm
 *
 * @property type Search type
 * @property value Search text
 * @constructor Create DeviceActivitySearchTerm
 */
data class DeviceActivitySearchTerm(
    val type: Type,
    val value: String
) {
    enum class Type {
        @SerializedName("text")
        TEXT
    }
}
