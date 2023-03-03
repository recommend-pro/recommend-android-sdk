package com.recommend.sdk.recommendation.data.model

import com.google.gson.annotations.SerializedName

/**
 * Recommendation panel
 *
 * @property panelId
 * @property slotId
 * @property html
 * @property data
 * @constructor Create empty Recommendation panel
 */
data class RecommendationPanel(
    @SerializedName("panel_id") val panelId: String,
    @SerializedName("slot_id") val slotId: String?,
    val html: String?,
    val data: RecommendationPanelData?
)
