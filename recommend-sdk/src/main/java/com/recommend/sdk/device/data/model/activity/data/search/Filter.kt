package com.recommend.sdk.device.data.model.activity.data.search

import com.google.gson.annotations.SerializedName

/**
 * Filter
 *
 * @property attributeCode
 * @constructor Create empty Filter
 */
data class Filter(
    @SerializedName("attribute_code") val attributeCode: String
)
