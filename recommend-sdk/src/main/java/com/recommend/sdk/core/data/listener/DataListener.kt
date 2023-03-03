package com.recommend.sdk.core.data.listener

import com.recommend.sdk.core.data.RequestTask

class DataListener<T>(
    val successCallback: (response: T?, requestTask: RequestTask<T>) -> Unit,
    val errorCallback: ((throwable: Throwable, requestTask: RequestTask<T>) -> Unit)? = null
)
