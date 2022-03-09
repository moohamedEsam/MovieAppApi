package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListsResponse(
    @SerialName("page")
    var page: Int? = null,
    @SerialName("results")
    var userLists: List<UserList>? = null,
    @SerialName("total_pages")
    var totalPages: Int? = null,
    @SerialName("total_results")
    var totalResults: Int? = null
)