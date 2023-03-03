package com.recommend.sdk.device.data.model.activity.data

import com.recommend.sdk.device.data.model.activity.data.source.Source

/**
 * Product click activity data
 *
 * @property sku Master product identifier of product that was clicked
 * @property products Products, which were shown together with product that was clicked
 * @property source Click source
 * @constructor Create empty Product click activity data
 */
data class DeviceProductClickActivityData(
    val sku: String,
    val products: List<DeviceActivityProduct>,
    val source: Source
)
