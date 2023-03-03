package com.recommend.sdk.recommendation.data.model

import com.google.gson.annotations.SerializedName

/**
 * Recommendation panel data
 *
 * @property locale
 * @property panelId
 * @property products
 * @property requestCountry
 * @constructor Create empty Recommendation panel data
 */
data class RecommendationPanelData(
    val locale: String,
    @SerializedName("panel_id") val panelId: String?,
    val products: List<RecommendationPanelProduct>,
    @SerializedName("request_country") val requestCountry: String?
)
