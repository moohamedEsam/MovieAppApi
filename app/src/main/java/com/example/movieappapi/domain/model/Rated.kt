package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rated(
    @SerialName("value")
    var value: Float? = null,

    @SerialName("is_rated")
    var isRated: Boolean? = null
)