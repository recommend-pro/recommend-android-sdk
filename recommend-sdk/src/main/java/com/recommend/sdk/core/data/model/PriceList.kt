package com.recommend.sdk.core.data.model

import com.google.gson.annotations.SerializedName

data class PriceList(
    val code: String,
    @SerializedName("fallback_mode") val fallbackMode: FallbackMode,
    val alternatives: List<String>
) {
    enum class FallbackMode {
        @SerializedName("none") NONE,
        @SerializedName("default") DEFAULT,
        @SerializedName("alternative") ALTERNATIVE
    }
}
