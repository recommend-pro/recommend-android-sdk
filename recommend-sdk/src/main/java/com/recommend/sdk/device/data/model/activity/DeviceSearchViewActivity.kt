package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceActivityProduct
import com.recommend.sdk.device.data.model.activity.data.DeviceSearchViewActivityData
import com.recommend.sdk.device.data.model.activity.data.search.DeviceActivitySearchTerm
import com.recommend.sdk.device.data.model.activity.data.search.Filter

/**
 * Search view activity. Send on search page opening.
 *
 * @param products Products, that were shown
 * @param term Search term
 * @param filter
 * @constructor Create Search view activity
 */
class DeviceSearchViewActivity(
    products: List<DeviceActivityProduct>,
    term: DeviceActivitySearchTerm,
    filter: List<Filter>
): BaseActivity {
    private val data = DeviceSearchViewActivityData(
        products,
        term,
        filter
    )

    companion object {
        const val TYPE = "search_view"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
