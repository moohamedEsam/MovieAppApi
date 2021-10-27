package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCreatedLists(
    @SerialName("page")
    var page: Int? = null,
    @SerialName("results")
    var userLists: List<UserList>? = null,
    @SerialName("total_pages")
    var totalPages: Int? = null,
    @SerialName("total_results")
    var totalResults: Int? = null
)