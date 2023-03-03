package com.recommend.sdk

import com.recommend.sdk.core.data.api.UniqueIdTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Invocation
import retrofit2.Response
import kotlin.reflect.jvm.javaMethod

class TestCall<T>(
    private val onSuccess: suspend () -> T,
    private val result: T,
    private val onError: (suspend () -> Throwable)? = null
): Call<T> {
    private var isExecuted: Boolean = false
    private var isCanceled: Boolean = false
    private var job: Job? = null

    override fun clone(): Call<T> {
        return TestCall(
            onSuccess = onSuccess,
            result = result,
            onError = onError
        )
    }

    override fun execute(): Response<T> {
        isExecuted = true
        return Response.success(result)
    }

    override fun enqueue(callback: Callback<T>) {
        isExecuted = true
        job = GlobalScope.launch {
            try {
                if (onError != null) {
                    val defError = GlobalScope.async { onError!!() }
                    callback.onFailure(this@TestCall, defError.await())
                } else {
                    val defSuccess = GlobalScope.async { onSuccess() }
                    callback.onResponse(this@TestCall, Response.success(defSuccess.await()))
                }
            } catch (e: Throwable) {
                callback.onFailure(this@TestCall, e)
            }
        }
    }

    override fun isExecuted(): Boolean {
        return isExecuted
    }

    override fun cancel() {
        isCanceled = true
        job?.cancel()
    }

    override fun isCanceled(): Boolean {
        return isCanceled
    }

    override fun request(): Request {
        val builder = Request.Builder()
        builder.url("https://test.com")
        builder.tag(Invocation::class.java, Invocation.of(this::invocationMethod.javaMethod!!, listOf(
            UniqueIdTag()
        )))
        return builder.build()
    }

    override fun timeout(): Timeout {
        return Timeout()
    }

    fun invocationMethod() {}
}
