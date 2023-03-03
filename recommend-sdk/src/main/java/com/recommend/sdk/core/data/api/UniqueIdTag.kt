package com.recommend.sdk.core.data.api

import java.util.*

data class UniqueIdTag(
    val id: String = UUID.randomUUID().toString()
)
