package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Product rated activity data
 *
 * @property sku Master product identifier
 * @property rate Product rate from 0 to 1
 * @property variationSku Simple (size) product identifier
 * @constructor Create empty Product rated activity data
 */
data class DeviceProductRatedActivityData(
    val sku: String,
    val rate: Float,
    @SerializedName("variation_sku") val variationSku: String
)
