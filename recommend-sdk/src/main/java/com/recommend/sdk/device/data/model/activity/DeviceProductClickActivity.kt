package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceActivityProduct
import com.recommend.sdk.device.data.model.activity.data.DeviceProductClickActivityData
import com.recommend.sdk.device.data.model.activity.data.source.Source

/**
 * Product click activity
 *
 * @param sku Master product identifier of product that was clicked
 * @param products Products, which were shown together with product that was clicked
 * @param source Click source
 * @constructor Create empty Product click activity
 */
class DeviceProductClickActivity(
    val sku: String,
    val products: List<DeviceActivityProduct>,
    val source: Source
): BaseActivity {
    private val data = DeviceProductClickActivityData(
        sku,
        products,
        source
    )

    companion object {
        const val TYPE = "product_click"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
