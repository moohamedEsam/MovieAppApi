package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllSearchResponse(
    @SerialName("page")
    var page: Int? = null,
    @SerialName("results")
    var searchResults: List<SearchResult>? = null,
    @SerialName("total_pages")
    var totalPages: Int? = null,
    @SerialName("total_results")
    var totalResults: Int? = null
)