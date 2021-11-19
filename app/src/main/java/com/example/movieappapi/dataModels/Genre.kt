package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("name")
    var name: String? = null
)