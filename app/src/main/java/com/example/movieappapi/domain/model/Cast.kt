package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cast(
    @SerialName("adult")
    var adult: Boolean? = null,
    @SerialName("cast_id")
    var castId: Int? = null,
    @SerialName("character")
    var character: String? = null,
    @SerialName("credit_id")
    var creditId: String? = null,
    @SerialName("gender")
    var gender: Int? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("known_for_department")
    var knownForDepartment: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("order")
    var order: Int? = null,
    @SerialName("original_name")
    var originalName: String? = null,
    @SerialName("popularity")
    var popularity: Double? = null,
    @SerialName("profile_path")
    var profilePath: String? = null
)