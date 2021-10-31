package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowsResponse(
    @SerialName("page")
    var page: Int? = null,
    @SerialName("results")
    var results: List<TvShow>? = null,
    @SerialName("total_pages")
    var totalPages: Int? = null,
    @SerialName("total_results")
    var totalResults: Int? = null
)