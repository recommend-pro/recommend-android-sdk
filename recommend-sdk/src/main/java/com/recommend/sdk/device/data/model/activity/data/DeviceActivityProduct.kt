package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * DeviceActivityProduct
 *
 * @property sku Master product identifier
 * @property variationSku Simple (size) product identifier
 * @property position Product position
 * @constructor Create empty Product
 */
data class DeviceActivityProduct(
    val sku: String,
    @SerializedName("variation_sku") val variationSku: String?,
    val position: Int
)
