package com.recommend.sdk

import androidx.test.core.app.ApplicationProvider
import com.recommend.sdk.core.data.ApiManager
import com.recommend.sdk.core.data.ApiTask
import com.recommend.sdk.core.data.RequestTask
import com.recommend.sdk.core.data.api.ApiServiceBuilder
import com.recommend.sdk.core.data.listener.DataListener
import com.recommend.sdk.core.data.model.Metrics
import com.recommend.sdk.core.data.util.ApiHelper
import com.recommend.sdk.core.util.DateTimeHelper
import com.recommend.sdk.core.util.RecommendLogger
import com.recommend.sdk.device.data.api.ApiDeviceService
import com.recommend.sdk.device.data.model.activity.DeviceOpenAppActivity
import com.recommend.sdk.device.data.api.request.DeviceActivityRequest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class ApiManagerTest {
    private val apiManager = ApiManager(ApplicationProvider.getApplicationContext(), RecommendLogger())
    private val apiTrackerService = ApiServiceBuilder.getService(
        "testAppId",
        "https://test-api-host.com",
        ApiHelper(
            ApplicationProvider.getApplicationContext()
        ),
        ApiDeviceService::class.java,
        RecommendLogger()
    )

    @Test
    fun `requests success one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )

        val future: CompletableFuture<String> = CompletableFuture()
        val task = ApiTask(
            RequestTask(
                call = TestCall(
                    suspend {
                        Thread.sleep(100)
                        testResponse
                    },
                    testResponse
                ),
                dataListener = DataListener(
                    { response, _ ->
                        future.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task)

        Assert.assertEquals(future.get(), testResponse.result)
    }

    @Test
    fun `requests failed one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )
        val errorMessage = "failed message"

        val future: CompletableFuture<String> = CompletableFuture()
        val task = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(100)
                        throw RuntimeException(errorMessage)
                        testResponse
                    },
                    testResponse
                ),
                DataListener(
                    { _, _ ->},
                    { response, _ ->
                        future.complete(response.message)
                    }
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task)

        Assert.assertEquals(future.get(), errorMessage)
    }

    @Test
    fun `requests error one in queue`() {
        val testResponse = TestResponse(
            true,
            "check requests queue"
        )
        val errorMessage = "error mssage"

        val future: CompletableFuture<String> = CompletableFuture()
        val task = ApiTask(
            RequestTask(
                TestCall(
                    suspend {},
                    testResponse,
                    onError = suspend {
                        Thread.sleep(50)
                        RuntimeException(errorMessage)
                    }
                ),
                DataListener(
                    { _, _ ->},
                    { response, _ ->
                        future.complete(response.message)
                    }
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task)

        Assert.assertEquals(future.get(), errorMessage)
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
        val task1 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(50)
                        result.add(testResponse1.result)
                        testResponse1
                    },
                    testResponse1
                ),
                DataListener(
                    { response, _ ->
                        future1.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(200)
                        result.add(testResponse2.result)
                        testResponse2
                    },
                    testResponse2
                ),
                DataListener(
                    { response, _ ->
                        future2.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(150)
                        result.add(testResponse3.result)
                        testResponse3
                    },
                    testResponse3
                ),
                DataListener(
                    { response, _ ->
                        future3.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(100)
                        result.add(testResponse4.result)
                        testResponse4
                    },
                    testResponse4
                ),
                DataListener(
                    { response, _ ->
                        future4.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task1)
        apiManager.processTask(task2)
        apiManager.processTask(task3)
        apiManager.processTask(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        Assert.assertArrayEquals(
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
        val task1 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(50)
                        result.add(testResponse1.result)
                        testResponse1
                    },
                    testResponse1
                ),
                DataListener(
                    { response, _ ->
                        future1.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = false,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(200)
                        result.add(testResponse2.result)
                        testResponse2
                    },
                    testResponse2
                ),
                DataListener(
                    { response, _ ->
                        future2.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = false,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(150)
                        result.add(testResponse3.result)
                        testResponse3
                    },
                    testResponse3
                ),
                DataListener(
                    { response, _ ->
                        future3.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = false,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(100)
                        result.add(testResponse4.result)
                        testResponse4
                    },
                    testResponse4
                ),
                DataListener(
                    { response, _ ->
                        future4.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = false,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task1)
        apiManager.processTask(task2)
        apiManager.processTask(task3)
        apiManager.processTask(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        Assert.assertArrayEquals(
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
        val task1 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(50)
                        result.add(testResponse1.result)
                        testResponse1
                    },
                    testResponse1
                ),
                DataListener(
                    { response, _ ->
                        future1.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future2: CompletableFuture<String> = CompletableFuture()
        val task2 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(200)
                        result.add(testResponse2.result)
                        testResponse2
                    },
                    testResponse2
                ),
                DataListener(
                    { response, _ ->
                        future2.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = false,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future3: CompletableFuture<String> = CompletableFuture()
        val task3 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(100)
                        result.add(testResponse3.result)
                        testResponse3
                    },
                    testResponse3
                ),
                DataListener(
                    { response, _ ->
                        future3.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )
        val future4: CompletableFuture<String> = CompletableFuture()
        val task4 = ApiTask(
            RequestTask(
                TestCall(
                    suspend {
                        Thread.sleep(150)
                        result.add(testResponse4.result)
                        testResponse4
                    },
                    testResponse4
                ),
                DataListener(
                    { response, _ ->
                        future4.complete(response?.result)
                    },
                    { _, _ ->}
                ),
                inTurn = true,
            ),
            repeatConfig = ApiTask.RepeatConfig()
        )

        apiManager.processTask(task1)
        apiManager.processTask(task2)
        apiManager.processTask(task3)
        apiManager.processTask(task4)
        future1.get()
        future2.get()
        future3.get()
        future4.get()

        Assert.assertArrayEquals(
            result.toTypedArray(),
            arrayOf(
                testResponse1.result,
                testResponse3.result,
                testResponse2.result,
                testResponse4.result
            )
        )
    }

    private val deviceActivityRequest = DeviceActivityRequest(
        "testCustomerIdHash",
        "testStore",
        "testCurrency",
        "testEnvironment",
        "test",
        DateTimeHelper.getDeviceTime(),
        DateTimeHelper.getCurrentTime(),
        Metrics(
            true,
            listOf(Metrics.Metric("delivery_country", "testDeliveredCountry"))
        ),
        listOf(DeviceActivityRequest.ActivityRequest("test_type"))
    )
    private val testApiTask = listOf(
        ApiTask(
            RequestTask(
                apiTrackerService.trackActivity(
                    "testDeviceId",
                    deviceActivityRequest
                ),
                DataListener({ _, _ ->}, { _, _ ->}),
                true,
            ),
            ApiTask.RepeatConfig(
                needRepeatWhenNoInternetConnection = true,
                needRepeatOnUnsuccess = true,
                repeatPattern = listOf(
                    Pair(2, TimeUnit.SECONDS),
                    Pair(3, TimeUnit.SECONDS),
                    Pair(4, TimeUnit.SECONDS)
                ),
                repeatInterval = 300,
                repeatIntervalTimeUnit = TimeUnit.SECONDS
            )
        )
    )

    @Test
    fun `check api task serialization`() {
        val apiTask = testApiTask[0]

        val repeatableApiTask = apiTask.toRepeatableApiTask()

        assertEquals(
            repeatableApiTask,
            ApiTask.RepeatableApiTask.deserializedFromJson(repeatableApiTask.serializedToJson())
        )
    }
}
