package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceProductViewActivityData

/**
 * Product view activity. Send on PDP opening.
 *
 * @param sku Master product identifier
 * @param variationSku Simple (size) product identifier
 * @constructor Create Product view activity
 */
class DeviceProductViewActivity(
    sku: String,
    variationSku: String?
): BaseActivity {
    private val data = DeviceProductViewActivityData(
        sku,
        variationSku
    )

    companion object {
        const val TYPE = "product_view"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
