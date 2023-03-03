package com.recommend.sdk.device.data.model.activity.data

import com.recommend.sdk.device.data.model.activity.data.search.Filter
import com.recommend.sdk.device.data.model.activity.data.search.DeviceActivitySearchTerm

/**
 * Search view activity data
 *
 * @property products Products, that were shown
 * @property term Search term
 * @property filter
 * @constructor Create Search view activity data
 */
data class DeviceSearchViewActivityData(
    val products: List<DeviceActivityProduct>,
    val term: DeviceActivitySearchTerm,
    val filter: List<Filter>
)
