package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCredits(
    @SerialName("cast")
    var cast: List<Cast>? = null,
    @SerialName("crew")
    var crew: List<Crew>? = null,
    @SerialName("id")
    var id: Int? = null
)