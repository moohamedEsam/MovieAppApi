package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListDetailsResponse(
    @SerialName("created_by")
    var createdBy: String? = null,
    @SerialName("description")
    var description: String? = null,
    @SerialName("favorite_count")
    var favoriteCount: Int? = null,
    @SerialName("id")
    var id: String? = null,
    @SerialName("iso_639_1")
    var iso6391: String? = null,
    @SerialName("item_count")
    var itemCount: Int? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("items")
    var items: List<Movie>?
)