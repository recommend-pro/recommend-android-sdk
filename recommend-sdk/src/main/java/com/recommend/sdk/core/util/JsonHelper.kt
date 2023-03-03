package com.recommend.sdk.core.util

import com.google.gson.*
import java.lang.reflect.Type

class JsonHelper {
    companion object {
        fun toJson(obj: Any): String {
            return getGson().toJson(obj)
        }

        fun <T> fromJson(json: String?, mapType: Class<T>): T {
            return getGson().fromJson(json, mapType)
        }

        fun <T> fromJson(json: String?, mapType: Type): T {
            return getGson().fromJson(json, mapType)
        }

        fun <T> fromJson(json: JsonElement?, mapType: Type): T {
            return getGson().fromJson(json, mapType)
        }

        fun <T> fromJson(json: JsonElement, mapType: Class<T>): T {
            return getGson().fromJson(json, mapType)
        }

        fun getGson(): Gson {
            return GsonBuilder()
                .registerTypeAdapterFactory(SerializableAsNullConverter())
                .create()
        }
    }
}
