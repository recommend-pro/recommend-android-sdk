package com.recommend.sdk.device.data.api.request

import com.google.gson.annotations.SerializedName
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.util.SerializeNull
import com.recommend.sdk.device.data.model.activity.data.DeviceTime

data class DeviceActivityRequest(
    @SerializeNull @SerializedName("customer_id_hash") val customerIdHash: String?,
    val store: String?,
    val currency: String?,
    val environment: String?,
    @SerializedName("price_list") val priceList: String?,
    @SerializedName("device_time") val deviceTime: DeviceTime,
    @SerializedName("event_time") val eventTime: Long,
    val metrics: Metrics?,
    val activity: List<ActivityRequest>
) {
    data class ActivityRequest(
        val type: String,
        val data: Any? = null
    )
}
