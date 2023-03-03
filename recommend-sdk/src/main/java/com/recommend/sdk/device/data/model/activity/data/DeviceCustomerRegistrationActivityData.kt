package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName
import com.recommend.sdk.core.util.SerializeNull

/**
 * Device customer registration activity data
 *
 * @property requestId Backend API request identifier linked with activity. Request_id can be omitted.
 * This field is needed in case the api does not work correctly for some reason (for example, there is too much delay between sending requests to update orders / wishlists / carts).
 * @constructor Create Device customer registration activity data
 */
data class DeviceCustomerRegistrationActivityData(
    @SerializeNull @SerializedName("request_id") val requestId: String? = null
)
