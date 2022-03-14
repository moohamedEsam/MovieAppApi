package com.example.movieappapi.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BelongsToCollection(
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("poster_path")
    var posterPath: String? = null
)