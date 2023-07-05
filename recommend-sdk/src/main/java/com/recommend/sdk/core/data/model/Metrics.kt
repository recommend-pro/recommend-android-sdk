package com.recommend.sdk.core.data.model

import com.google.gson.annotations.SerializedName

data class Metrics(
    val data: List<Metric>,
    @SerializedName("non_interactive") val nonInteractive: Boolean = false
) {
    data class Metric(
        val code: String,
        val value: String
    )
}
