package com.recommend.sdk.device.data.model.activity

import com.google.gson.annotations.SerializedName
import com.recommend.sdk.device.data.model.activity.data.DeviceActivityProduct
import com.recommend.sdk.device.data.model.activity.data.DeviceElasticListActivityData

/**
 * Elastic list activity. Send on elastic page opening.
 *
 * @param products Products, which were shown
 * @param elasticListId Elastic listing identifier
 * @constructor Create empty Elastic list activity
 */
class DeviceElasticListActivity(
    val products: List<DeviceActivityProduct>,
    @SerializedName("elastic_list_id") val elasticListId: String
): BaseActivity {
    private val data = DeviceElasticListActivityData(
        products,
        elasticListId
    )

    companion object {
        const val TYPE = "elastic_list"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any {
        return data
    }
}
