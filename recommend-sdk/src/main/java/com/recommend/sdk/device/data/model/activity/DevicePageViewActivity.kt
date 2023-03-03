package com.recommend.sdk.device.data.model.activity

import com.recommend.sdk.device.data.model.activity.data.DevicePageViewActivityData

/**
 * Page view activity. Send this event on open any page.
 *
 * @param url Page URL
 * @param pageType Page type
 * @param referrer Page referrer (URL)
 * @param title Page title, equal to document.title
 * @constructor Create Page view activity
 */
class DevicePageViewActivity(
    url: String,
    pageType: String,
    referrer: String,
    title: String
): BaseActivity {
    private val data = DevicePageViewActivityData(
        url,
        pageType,
        referrer,
        title
    )

    companion object {
        const val TYPE = "pageview"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getData(): Any? {
        return data
    }
}
