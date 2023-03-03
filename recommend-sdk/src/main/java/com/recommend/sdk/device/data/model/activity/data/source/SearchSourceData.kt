package com.recommend.sdk.device.data.model.activity.data.source

import com.recommend.sdk.device.data.model.activity.data.search.DeviceActivitySearchTerm

/**
 * Search source
 *
 * @property term Search type and search text
 * @constructor Create Search source
 */
data class SearchSourceData(
    val term: DeviceActivitySearchTerm
): BaseSourceData()
