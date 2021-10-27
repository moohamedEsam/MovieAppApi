package com.example.movieappapi.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserList(
    @SerialName("description")
    var description: String? = null,
    @SerialName("favorite_count")
    var favoriteCount: Int? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("iso_639_1")
    var iso6391: String? = null,
    @SerialName("item_count")
    var itemCount: Int? = null,
    @SerialName("list_type")
    var listType: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("poster_path")
    var posterPath: String? = null
)