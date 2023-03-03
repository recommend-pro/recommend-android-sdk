package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Panel view activity data
 *
 * @property products Products, which were shown
 * @property panelId Panel identifier
 * @property productsCount
 * @constructor Create empty Panel view activity data
 */
data class DevicePanelViewActivityData(
    val products: List<DeviceActivityProduct>,
    @SerializedName("panel_id") val panelId: String,
    @SerializedName("products_count") val productsCount: Int
)
