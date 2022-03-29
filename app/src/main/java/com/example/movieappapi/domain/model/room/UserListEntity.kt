package com.example.movieappapi.domain.model.room

data class UserListEntity(
    var id: Int,
    var description: String,
    var favoriteCount: Int,
    var itemCount: Int,
    var name: String,
    var posterPath: String?
)