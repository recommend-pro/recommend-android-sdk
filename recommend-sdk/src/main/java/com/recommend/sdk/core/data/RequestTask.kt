package com.recommend.sdk.core.data

import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.data.util.ApiHelper
import retrofit2.Call
import java.lang.RuntimeException

data class RequestTask<T>(
    val call: Call<T>,
    val dataListener: DataListener<T>? = null,
    val inTurn: Boolean = true
) {
    val uuid: String

    init {
        val uniqueIdTag = ApiHelper.getUniqueIdTagFromRequest(call.request())
            ?: throw RuntimeException("All requests must contain UniqueIdTag as argument")

        uuid = uniqueIdTag.id
    }
}
