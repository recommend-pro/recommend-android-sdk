package com.recommend.sdk.core.data.model

import com.google.gson.annotations.SerializedName

data class Metrics(
    @SerializedName("non_interactive") val nonInteractive: Boolean?,
    val data: List<Metric>
) {
    data class Metric(
        val code: String,
        val value: String
    )
}
