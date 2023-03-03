package com.recommend.sdk.recommendation.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Recommendation panels error response
 *
 * @property success
 * @property errorCode
 * @property errorMessage
 * @constructor Create Recommendation panels error response
 */
data class RecommendationPanelsErrorResponse(
    val success: Boolean,
    @SerializedName("error_code") val errorCode: String,
    @SerializedName("error_message") val errorMessage: String
)
