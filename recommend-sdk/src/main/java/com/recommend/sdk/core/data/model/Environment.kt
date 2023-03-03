package com.recommend.sdk.core.data.model

data class Environment(
    val customerIdHash: String? = null,
    val customerEmailHash: String? = null,
    val store: String? = null,
    val currency: String? = null,
    val environment: String? = null,
    val priceList: PriceList? = null
)
