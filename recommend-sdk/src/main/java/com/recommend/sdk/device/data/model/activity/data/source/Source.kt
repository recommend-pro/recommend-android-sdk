package com.recommend.sdk.device.data.model.activity.data.source

import com.google.gson.annotations.SerializedName

/**
 * Base source
 *
 * @property type Source type
 * @constructor Create empty Base source
 */
data class Source(
    val type: SourceType,
    val data: BaseSourceData
) {
    enum class SourceType {
        @SerializedName("list")
        LIST,
        @SerializedName("panel")
        PANEL,
        @SerializedName("search")
        SEARCH
    }
}
