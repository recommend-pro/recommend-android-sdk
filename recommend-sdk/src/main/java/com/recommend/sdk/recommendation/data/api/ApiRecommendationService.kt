package com.recommend.sdk.recommendation.data.api

import com.recommend.sdk.core.data.api.UniqueIdTag
import com.recommend.sdk.recommendation.data.api.request.RecommendationPanelsRequest
import com.recommend.sdk.recommendation.data.api.response.RecommendationPanelsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiRecommendationService {
    @POST("recommendation/panel")
    fun getPanel(
        @Body request: RecommendationPanelsRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Tag uniqueIdTag: UniqueIdTag = UniqueIdTag()
    ): Call<RecommendationPanelsResponse>
}
