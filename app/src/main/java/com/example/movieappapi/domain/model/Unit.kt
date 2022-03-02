package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Unit(
    @SerialName("expires_at")
    var expiresAt: String? = null,
    @SerialName("request_token")
    var requestToken: String? = null,
    @SerialName("guest_session_id")
    var guestSessionId: String? = null,
    @SerialName("success")
    var success: Boolean? = null
)