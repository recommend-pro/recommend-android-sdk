package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DeviceAddToCartActivityData

/**
 * Add to cart activity. Send after product is added to cart.
 *
 * @param cartHash Hashed cart identifier SHA256
 * @param sku Master product identifier
 * @param variationSku Simple (size) product identifier
 * @param requestId Backend API request identifier linked with activity. Recommend: request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts)
 * @constructor Create Add to cart activity
 */
class DeviceAddToCartActivity(
    cartHash: String,
    sku: String,
    variationSku: String,
    requestId: String? = null
): BaseActivity {
    private val data = DeviceAddToCartActivityData(
        cartHash,
        sku,
        variationSku,
        requestId
    )

    companion object {
        const val TYPE = "add_to_cart"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
