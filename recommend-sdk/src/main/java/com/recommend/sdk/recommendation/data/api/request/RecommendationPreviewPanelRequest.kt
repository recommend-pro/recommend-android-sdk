package com.recommend.sdk.recommendation.data.api.request

/**
 * Recommendation preview panel request
 *
 * @property global
 * @property ids example: List [ "panel-1", "panel-2" ]
 * @constructor Create Recommendation preview panel request
 */
data class RecommendationPreviewPanelRequest(
    val global: Boolean,
    val ids: List<String>
)
