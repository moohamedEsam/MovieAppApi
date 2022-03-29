package com.example.movieappapi.domain.utils.mappers

import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.UserListDetailsResponse
import com.example.movieappapi.domain.model.room.UserListDetailsEntity

fun UserListDetailsResponse.toUserListDetailsEntity() = UserListDetailsEntity(
    id = id?.toInt() ?: 0,
    description = description ?: "",
    favoriteCount = favoriteCount ?: 0,
    itemCount = itemCount ?: 0,
    name = name ?: "",
    posterPath = posterPath ?: "",
    items = items?.map { it.id ?: 0 }
)

suspend fun UserListDetailsEntity.toUserListDetailsResponse(movies: suspend (List<Int>) -> List<Movie>) =
    UserListDetailsResponse(
        id = id.toString(),
        description = description,
        favoriteCount = favoriteCount,
        itemCount = itemCount,
        items = movies(items ?: emptyList()),
        name = name,
        posterPath = posterPath
    )