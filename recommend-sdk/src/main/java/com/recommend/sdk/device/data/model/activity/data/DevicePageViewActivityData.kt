package com.recommend.sdk.device.data.model.activity.data

import com.google.gson.annotations.SerializedName

/**
 * Page view activity data
 *
 * @property url Page URL
 * @property pageType Page type
 * @property referrer Page referrer (URL)
 * @property title Page title, equal to document.title
 * @constructor Create Page view activity data
 */
data class DevicePageViewActivityData (
    val url: String,
    @SerializedName("page_type") val pageType: String,
    val referrer: String,
    val title: String
)