package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RateMediaResponse(
    @SerialName("status_code")
    var statusCode: Int? = null,
    @SerialName("status_message")
    var statusMessage: String? = null
)