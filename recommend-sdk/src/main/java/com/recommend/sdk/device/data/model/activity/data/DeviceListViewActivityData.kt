package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * List view activity data
 *
 * @property products Products, which were shown
 * @property listId Listing (category) identifier
 * @constructor Create empty List view activity data
 */
data class DeviceListViewActivityData(
    val products: List<DeviceActivityProduct>,
    @SerializedName("list_id") val listId: String
)
