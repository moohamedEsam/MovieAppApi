package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompany(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("logo_path")
    var logoPath: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("origin_country")
    var originCountry: String? = null
)