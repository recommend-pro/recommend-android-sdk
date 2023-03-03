package com.recommend.sdk.device.data.model.activity.data.source

import com.google.gson.annotations.SerializedName

/**
 * List source data
 *
 * @property listId Listing identifier
 * @constructor Create List source data
 */
data class ListSourceData(
    @SerializedName("list_id") val listId: String
): BaseSourceData()
