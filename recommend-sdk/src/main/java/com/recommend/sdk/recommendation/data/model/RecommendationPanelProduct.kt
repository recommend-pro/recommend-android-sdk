package com.recommend.sdk.recommendation.data.model

import com.google.gson.annotations.SerializedName

/**
 * Recommendation panel product
 *
 * @property id
 * @property name
 * @property trimedName
 * @property sku
 * @property status
 * @property lists
 * @property price
 * @property originalPrice
 * @property image
 * @property url
 * @property attrs
 * @constructor Create empty Recommendation panel product
 */
data class RecommendationPanelProduct(
    val id: String?,
    val name: String,
    @SerializedName("trimed_name") val trimedName: String?,
    val sku: String,
    val status: Boolean,
    val lists: List<String>,
    val price: Double,
    @SerializedName("original_price") val originalPrice: Double,
    val image: String,
    val url: String,
    val attrs: Map<String, Attribute>?
) {
    data class Attribute(
        @SerializedName("val") val value: Any
    )
}
