package com.recommend.sdk.recommendation.data.api.request

import com.google.gson.annotations.SerializedName


/**
 * Recommendation panel request
 *
 * @property id
 * @property context
 * @property filter Filter by product attribute
 * @property attrsToInclude min items count = 1
 * example: List [ "gender" ]
 * Product attribute codes should be included for content_type "json" to receive a response containing product attributes values along with their codes.
 * Otherwise, attributes will not be included to the response.
 * @property variations
 * @constructor Create Recommendation panel request
 */
data class RecommendationPanelRequest(
    val id: String,
    val context: RecommendationPanelRequestContext? = null,
    @SerializedName("filter") val filters: List<RecommendationPanelRequestFilter>? = null,
    @SerializedName("attrs_to_include") val attrsToInclude: List<String>? = null,
    val variations: Variations? = null
) {
    /**
     * Recommendation panel request context
     *
     * @property searchTerm
     * @property currentContext
     * @property skus min items count = 1, max items count = 5
     * @property listIds min items count = 1, max items count = 5
     * @property imageBoxId
     * @constructor Create Recommendation panel request context
     */
    data class RecommendationPanelRequestContext(
        @SerializedName("search_term") val searchTerm: String,
        @SerializedName("current") val currentContext: CurrentContext? = null,
        val skus: List<String>? = null,
        @SerializedName("list_ids") val listIds: List<String>? = null,
        @SerializedName("image_box_id") val imageBoxId: String? = null
    ) {
        data class CurrentContext(
            val sku: String,
            @SerializedName("list_id") val listId: String
        )
    }

    /**
     * Recommendation panel request filter
     *
     * @property type
     * @property code
     * @property operator
     * @property value
     * @constructor Create Recommendation panel request filter
     */
    data class RecommendationPanelRequestFilter(
        val type: Type,
        val code: String,
        val operator: String,
        val value: String
    ) {
        enum class Type {
            @SerializedName("product")
            PRODUCT
        }
    }

    /**
     * Variations
     *
     * @property include
     * @property attrsToInclude min items count = 1
     * example: List [ "gender"
     * Variation attribute codes should be included for content_type "json" to receive a response containing product attributes values along with their codes.
     * Otherwise, attributes will not be included to the response.
     * @constructor Create Variations
     */
    data class Variations(
        val include: Boolean,
        @SerializedName("attrs_to_include") val attrsToInclude: List<String>
    )
}
