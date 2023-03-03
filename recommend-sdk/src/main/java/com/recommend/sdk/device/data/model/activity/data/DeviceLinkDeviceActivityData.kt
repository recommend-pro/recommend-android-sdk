package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Link device event data
 *
 * @property deviceIdsToLink Device ids
 * @constructor Create Link device event data
 */
data class DeviceLinkDeviceActivityData(
    @SerializedName("device_ids_to_link") val deviceIdsToLink: List<String>
)
