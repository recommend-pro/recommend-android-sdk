package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Elastic list activity event
 *
 * @property products Products, which were shown
 * @property elasticListId Elastic listing identifier
 * @constructor Create Elastic list activity event
 */
data class DeviceElasticListActivityData(
    val products: List<DeviceActivityProduct>,
    @SerializedName("elastic_list_id") val elasticListId: String
)
