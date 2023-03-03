package com.recommend.sdk.device.data.model.activity.data.source

import com.google.gson.annotations.SerializedName

/**
 * Panel source data
 *
 * @property panelId Panel identifier
 * @constructor Create Panel source data
 */
data class PanelSourceData(
    @SerializedName("panel_id") val panelId: String
): BaseSourceData()
