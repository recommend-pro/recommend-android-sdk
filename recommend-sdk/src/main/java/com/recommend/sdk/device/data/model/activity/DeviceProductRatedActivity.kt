package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceProductRatedActivityData

/**
 * Product rated activity
 *
 * @param sku Master product identifier
 * @param rate Product rate from 0 to 1
 * @param variationSku Simple (size) product identifier
 * @constructor Create empty Product rated activity
 */
class DeviceProductRatedActivity(
    sku: String,
    rate: Float,
    variationSku: String
): BaseActivity {
    private val data = DeviceProductRatedActivityData(
        sku,
        rate,
        variationSku
    )
    companion object {
        const val TYPE = "product_rated"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
