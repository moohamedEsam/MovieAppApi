package com.example.movieappapi.domain.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userListDetailsEntity")
data class UserListDetailsEntity(
    @PrimaryKey
    var id: Int,
    var description: String,
    var favoriteCount: Int,
    var itemCount: Int,
    var name: String,
    var posterPath: String?,
    var items: List<Int>?
)