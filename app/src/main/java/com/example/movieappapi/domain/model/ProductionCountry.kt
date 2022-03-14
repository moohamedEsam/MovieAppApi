package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    var iso31661: String? = null,
    @SerialName("name")
    var name: String? = null
)