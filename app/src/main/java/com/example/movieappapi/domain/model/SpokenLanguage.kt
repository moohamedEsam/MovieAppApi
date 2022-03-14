package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguage(
    @SerialName("iso_639_1")
    var iso6391: String? = null,
    @SerialName("name")
    var name: String? = null
)