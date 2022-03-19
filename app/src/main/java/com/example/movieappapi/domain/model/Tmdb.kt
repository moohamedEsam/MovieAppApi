package com.example.movieappapi.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Tmdb {
    @SerialName("avatar_path")
    var avatarPath: String? = null
}