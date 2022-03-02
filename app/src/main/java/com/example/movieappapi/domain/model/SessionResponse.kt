package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    @SerialName("session_id")
    var sessionId: String? = null,
    @SerialName("success")
    var success: Boolean? = null
)