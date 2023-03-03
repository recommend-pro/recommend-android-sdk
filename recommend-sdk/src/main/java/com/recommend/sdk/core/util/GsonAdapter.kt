package com.recommend.sdk.core.util

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Field

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class SerializeNull

class SerializableAsNullConverter : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        fun Field.serializedName() = declaredAnnotations
            .filterIsInstance<SerializedName>()
            .firstOrNull()?.value ?: name
        val declaredFields = type.rawType.declaredFields
        val nullableFieldNames = declaredFields
            .filter { it.declaredAnnotations.filterIsInstance<SerializeNull>().isNotEmpty() }
            .map { it.serializedName() }
        val nonNullableFields = declaredFields.map { it.serializedName() } - nullableFieldNames.toSet()

        return object : TypeAdapter<T>() {
            private val delegateAdapter = gson.getDelegateAdapter(this@SerializableAsNullConverter, type)
            private val elementAdapter = gson.getAdapter(JsonElement::class.java)

            override fun write(writer: JsonWriter, value: T?) {
                if (nullableFieldNames.isEmpty()) {
                    if (delegateAdapter.toJsonTree(value).isJsonObject) {
                        writer.serializeNulls = false
                    }
                    elementAdapter.write(writer, delegateAdapter.toJsonTree(value))
                } else {
                    val jsonObject = delegateAdapter.toJsonTree(value).asJsonObject
                    nonNullableFields
                        .filter { jsonObject.get(it) is JsonNull }
                        .forEach { jsonObject.remove(it) }
                    val originalSerializeNulls = writer.serializeNulls
                    writer.serializeNulls = true
                    elementAdapter.write(writer, jsonObject)
                    writer.serializeNulls = originalSerializeNulls
                }
            }

            override fun read(reader: JsonReader): T {
                return delegateAdapter.read(reader)
            }
        }
    }
}
