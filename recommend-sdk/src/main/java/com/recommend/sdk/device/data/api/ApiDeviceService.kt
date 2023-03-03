package com.recommend.sdk.device.data.api

import com.recommend.sdk.core.data.api.UniqueIdTag
import com.recommend.sdk.device.data.api.request.DeviceActivityRequest
import com.recommend.sdk.device.data.api.response.DeviceActivityResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiDeviceService {
    @POST("device/{device_id}/activity")
    fun trackActivity(
        @Path("device_id") deviceId: String,
        @Body activity: DeviceActivityRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Tag uniqueIdTag: UniqueIdTag = UniqueIdTag()
    ): Call<DeviceActivityResponse>
}
