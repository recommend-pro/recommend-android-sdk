package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceActivityProduct
import com.recommend.sdk.device.data.model.activity.data.DevicePanelViewActivityData

/**
 * Panel view activity. Send when user sees recommendation panel. Activity should be sent every time when panel shows.
 *
 * @param products Products, which were shown
 * @param panelId Panel identifier
 * @param productsCount
 * @constructor Create Panel view activity
 */
class DevicePanelViewActivity(
    products: List<DeviceActivityProduct>,
    panelId: String,
    productsCount: Int
): BaseActivity {
    private val data = DevicePanelViewActivityData(
        products,
        panelId,
        productsCount
    )

    companion object {
        const val TYPE = "panel_view"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return data
    }
}
