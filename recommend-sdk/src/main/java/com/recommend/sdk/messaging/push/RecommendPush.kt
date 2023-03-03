package com.recommend.sdk.messaging.push

data class RecommendPush(
    val id: String,
    val title: String?,
    val body: String?,
    val rawMessageData: Map<String, String>,
    val url: String?,
    val image: String?,
)
