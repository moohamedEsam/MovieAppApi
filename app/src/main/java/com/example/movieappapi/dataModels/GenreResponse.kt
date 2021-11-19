package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genres")
    var genres: List<Genre>? = null
)