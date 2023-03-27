package com.recommend.sdk

import androidx.test.core.app.ApplicationProvider
import com.recommend.sdk.core.data.RequestManager
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.RecommendLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.RuntimeException
import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
class RequestManagerTest {
    private val requestManager = RequestManager(
        RecommendLogger(),
        ApiHelper(ApplicationProvider.getApplicationContext())
    )

    @Test
    fun `requests success one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )

        val future: CompletableFuture<String> = CompletableFuture()
        val task = RequestTask(
            TestCall(
                suspend {
                    sleep(100)
                    testResponse
                },
                testResponse
            ),
            DataListener(
                { response, _ ->
                    future.complete(response?.result)
                },
                { _, _ ->}
            )
        )

        requestManager.executeRequest(task)

        assertEquals(future.get(), testResponse.result)
    }

    @Test
    fun `requests failed one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )
        val errorMessage = "failed message"

        val future: CompletableFuture<String> = CompletableFuture()
        val task = RequestTask(
            TestCall(
                suspend {
                    sleep(100)
                    throw RuntimeException(errorMessage)
                    testResponse
                },
                testResponse
            ),
            DataListener(
                {_, _ ->},
                { error, _ ->
                    future.complete(error.message)
                }
            )
        )

        requestManager.executeRequest(task)

        assertEquals(future.get(), errorMessage)
    }

    @Test
    fun `requests error one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )
        val errorMessage = "error mssage"

        val future: CompletableFuture<String> = CompletableFuture()
        val task = RequestTask(
            TestCall(
                suspend {},
                testResponse,
                onError = suspend {
                    sleep(50)
                    RuntimeException(errorMessage)
                }
            ),
            DataListener(
                {_, _ ->},
                { response, _ ->
                    future.complete(response.message)
                }
            )
        )

        requestManager.executeRequest(task)

        assertEquals(future.get(), errorMessage)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `requests success many in queue`() {
        val testResponse1 = TestResponse(
            true,
            "check requests queue 1"
        )
        val testResponse2 = TestResponse(
            true,
            "check requests queue 2"
        )
        val testResponse3 = TestResponse(
            true,
            "check requests queue 3"
        )
        val testResponse4 = TestResponse(
            true,
            "check requests queue 4"
        )

        val result = mutableListOf<String>()

        val future1: CompletableFuture<String> = CompletableFuture()
        val task1 = RequestTask(
            TestCall(
                suspend {
                    sleep(50)
                    result.add(testResponse1.result)
                    testResponse1
                },
                testResponse1
            ),
            DataListener(
                { response, _ ->
                    future1.complete(response?.result)
                },
                {_, _ ->}
            )
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = RequestTask(
            TestCall(
                suspend {
                    sleep(200)
                    result.add(testResponse2.result)
                    testResponse2
                },
                testResponse2
            ),
            DataListener(
                { response, _ ->
                    future2.complete(response?.result)
                },
                {_, _ ->}
            )
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = RequestTask(
            TestCall(
                suspend {
                    sleep(150)
                    result.add(testResponse3.result)
                    testResponse3
                },
                testResponse3
            ),
            DataListener(
                { response, _ ->
                    future3.complete(response?.result)
                },
                {_, _ ->}
            )
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = RequestTask(
            TestCall(
                suspend {
                    sleep(100)
                    result.add(testResponse4.result)
                    testResponse4
                },
                testResponse4
            ),
            DataListener(
                { response, _ ->
                    future4.complete(response?.result)
                },
                {_, _ ->}
            )
        )

        requestManager.executeRequest(task1)
        requestManager.executeRequest(task2)
        requestManager.executeRequest(task3)
        requestManager.executeRequest(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        assertArrayEquals(
            result.toTypedArray(),
            arrayOf(
                testResponse1.result,
                testResponse2.result,
                testResponse3.result,
                testResponse4.result
            )
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `requests success many async`() {
        val testResponse1 = TestResponse(
            true,
            "check requests queue 1"
        )
        val testResponse2 = TestResponse(
            true,
            "check requests queue 2"
        )
        val testResponse3 = TestResponse(
            true,
            "check requests queue 3"
        )
        val testResponse4 = TestResponse(
            true,
            "check requests queue 4"
        )

        val result = mutableListOf<String>()

        val future1: CompletableFuture<String> = CompletableFuture()
        val task1 = RequestTask(
            TestCall(
                suspend {
                    sleep(50)
                    result.add(testResponse1.result)
                    testResponse1
                },
                testResponse1
            ),
            DataListener(
                { response, _ ->
                    future1.complete(response?.result)
                },
                {_, _ ->}
            ),
            inTurn = false
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = RequestTask(
            TestCall(
                suspend {
                    sleep(200)
                    result.add(testResponse2.result)
                    testResponse2
                },
                testResponse2
            ),
            DataListener(
                { response, _ ->
                    future2.complete(response?.result)
                },
                {_, _ ->}
            ),
            inTurn = false
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = RequestTask(
            TestCall(
                suspend {
                    sleep(150)
                    result.add(testResponse3.result)
                    testResponse3
                },
                testResponse3
            ),
            DataListener(
                { response, _ ->
                    future3.complete(response?.result)
                },
                {_, _ ->}
            ),
            inTurn = false
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = RequestTask(
            TestCall(
                suspend {
                    sleep(100)
                    result.add(testResponse4.result)
                    testResponse4
                },
                testResponse4
            ),
            DataListener(
                { response, _ ->
                    future4.complete(response?.result)
                },
                {_, _ ->}
            ),
            inTurn = false
        )

        requestManager.executeRequest(task1)
        requestManager.executeRequest(task2)
        requestManager.executeRequest(task3)
        requestManager.executeRequest(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        assertArrayEquals(
            result.toTypedArray(),
            arrayOf(
                testResponse1.result,
                testResponse4.result,
                testResponse3.result,
                testResponse2.result
            )
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `requests success many async and in queue`() {
        val testResponse1 = TestResponse(
            true,
            "check requests queue 1"
        )
        val testResponse2 = TestResponse(
            true,
            "check requests queue 2"
        )
        val testResponse3 = TestResponse(
            true,
            "check requests queue 3"
        )
        val testResponse4 = TestResponse(
            true,
            "check requests queue 4"
        )

        val result = mutableListOf<String>()

        val future1: CompletableFuture<String> = CompletableFuture()
        val task1 = RequestTask(
            TestCall(
                suspend {
                    sleep(50)
                    result.add(testResponse1.result)
                    testResponse1
                },
                testResponse1
            ),
            DataListener(
                { response, _ ->
                    future1.complete(response?.result)
                },
                {_, _ ->}
            )
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = RequestTask(
            TestCall(
                suspend {
                    sleep(200)
                    result.add(testResponse2.result)
                    testResponse2
                },
                testResponse2
            ),
            DataListener(
                { response, _ ->
                    future2.complete(response?.result)
                },
                {_, _ ->}
            ),
            inTurn = false
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = RequestTask(
            TestCall(
                suspend {
                    sleep(100)
                    result.add(testResponse3.result)
                    testResponse3
                },
                testResponse3
            ),
            DataListener(
                { response, _ ->
                    future3.complete(response?.result)
                },
                {_, _ ->}
            )
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = RequestTask(
            TestCall(
                suspend {
                    sleep(150)
                    result.add(testResponse4.result)
                    testResponse4
                },
                testResponse4
            ),
            DataListener(
                { response, _ ->
                    future4.complete(response?.result)
                },
                {_, _ ->}
            )
        )

        requestManager.executeRequest(task1)
        requestManager.executeRequest(task2)
        requestManager.executeRequest(task3)
        requestManager.executeRequest(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        assertArrayEquals(
            result.toTypedArray(),
            arrayOf(
                testResponse1.result,
                testResponse3.result,
                testResponse2.result,
                testResponse4.result
            )
        )
    }
}
