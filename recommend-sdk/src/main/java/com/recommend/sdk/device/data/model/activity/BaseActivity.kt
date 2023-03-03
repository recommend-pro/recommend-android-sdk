package com.recommend.sdk.device.data.model.activity

interface BaseActivity {
    fun getType(): String
    fun getData(): Any?
}
