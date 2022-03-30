package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeywordResponse(
    @SerialName("id")
    var id: Int? = null,
    @SerialName("keywords")
    var keywords: List<Keyword>? = null
)