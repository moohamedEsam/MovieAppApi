package com.example.movieappapi.domain.model


import com.example.movieappapi.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("expires_at")
    var expiresAt: String? = null,
    @SerialName("request_token")
    var requestToken: String? = null,
    @SerialName("success")
    var success: Boolean? = null
)

fun TokenResponse.toToken() = Token.newBuilder().setAccessToken(this.requestToken)
    .setExpiresAt(this.expiresAt)
    .build()

fun Token.toTokenResponse() = TokenResponse(this.accessToken, this.expiresAt)