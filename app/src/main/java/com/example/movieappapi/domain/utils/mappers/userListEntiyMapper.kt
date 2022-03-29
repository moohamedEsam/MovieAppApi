package com.example.movieappapi.domain.utils.mappers

import com.example.movieappapi.domain.model.UserList
import com.example.movieappapi.domain.model.UserListsResponse
import com.example.movieappapi.domain.model.room.UserListEntity

fun UserListEntity.toUserList() = UserList(
    description = description,
    favoriteCount = favoriteCount,
    id = id,
    itemCount = itemCount,
    name = name,
    posterPath = posterPath
)

fun List<UserListEntity>.toUserListsResponse() = UserListsResponse(
    userLists = this.map { it.toUserList() }
)