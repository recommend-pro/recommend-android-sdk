package com.recommend.sdk.recommendation.data.model

import com.google.gson.annotations.SerializedName

enum class PanelContentType {
    @SerializedName("json")
    JSON,
    @SerializedName("html")
    HTML,
}
