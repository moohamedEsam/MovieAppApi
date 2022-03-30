package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Keyword(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("name")
    var name: String? = null
)