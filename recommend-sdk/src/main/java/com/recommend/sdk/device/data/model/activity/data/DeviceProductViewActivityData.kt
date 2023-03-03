package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Product view activity data
 *
 * @property sku Master product identifier
 * @property variationSku Simple (size) product identifier
 * @constructor Create Product view activity data
 */
data class DeviceProductViewActivityData(
    val sku: String,
    @SerializedName("variation_sku") val variationSku: String?
)
