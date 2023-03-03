package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceActivityProduct
import com.recommend.sdk.device.data.model.activity.data.DeviceListViewActivityData

/**
 * List view activity. Send on plp opening.
 *
 * @param products Products, which were shown
 * @param listId Listing (category) identifier
 * @constructor Create List view activity
 */
class DeviceListViewActivity(
    products: List<DeviceActivityProduct>,
    listId: String
): BaseActivity {
    private val data = DeviceListViewActivityData(
        products,
        listId
    )

    companion object {
        const val TYPE = "list_view"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
