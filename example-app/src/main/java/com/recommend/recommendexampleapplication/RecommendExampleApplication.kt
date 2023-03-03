package com.recommend.recommendexampleapplication

import android.app.Application
import com.recommend.sdk.Recommend

class RecommendExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Recommend.init(
            this,
            "recommend_account_id",
            "recommend_application_id"
        )
    }
}
