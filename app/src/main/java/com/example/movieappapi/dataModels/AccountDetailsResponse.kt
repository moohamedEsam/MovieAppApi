package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsResponse(
    @SerialName("avatar")
    var avatar: Avatar? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("include_adult")
    var includeAdult: Boolean? = null,
    @SerialName("iso_3166_1")
    var iso31661: String? = null,
    @SerialName("iso_639_1")
    var iso6391: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("username")
    var username: String? = null
)