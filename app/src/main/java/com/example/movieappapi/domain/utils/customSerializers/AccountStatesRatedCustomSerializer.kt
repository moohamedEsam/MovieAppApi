package com.example.movieappapi.domain.utils.customSerializers

import com.example.movieappapi.domain.model.Rated
import kotlinx.serialization.json.*

object AccountStatesRatedCustomSerializer : JsonTransformingSerializer<Rated>(Rated.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonPrimitive)
            JsonObject(
                mapOf(
                    "value" to JsonPrimitive(0),
                    "is_rated" to JsonPrimitive(false)
                )
            )
        else {
            val value = element.jsonObject["value"] ?: JsonPrimitive(0)
            JsonObject(
                mapOf(
                    "value" to value,
                    "is_rated" to JsonPrimitive(true)
                )
            )
        }
    }
}