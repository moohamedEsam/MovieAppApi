package com.example.movieappapi.domain.model.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddMediaToWatchListRequestBody(
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("watchlist")
    val watchList: Boolean
)