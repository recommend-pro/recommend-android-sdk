package com.recommend.sdk.recommendation.data.api.response

import com.recommend.sdk.recommendation.data.model.RecommendationPanel

/**
 * Recommendation panels response
 *
 * @property success
 * @property result
 * @constructor Create Recommendation panels response
 */
data class RecommendationPanelsResponse(
    val success: Boolean,
    val result: List<RecommendationPanel>
)
