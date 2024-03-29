package com.example.movieappapi.domain.utils.mappers

import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.room.MovieDetailsEntity
import com.example.movieappapi.domain.model.room.MovieEntity
import java.util.*

fun Movie.toMovieEntity(dateAdded: Date? = null, tag: String = "popular") = MovieEntity(
    id = id ?: 0,
    posterPath = posterPath ?: "",
    title = title ?: "",
    dateAdded = dateAdded,
    tag = tag,
    rating = (voteAverage ?: 0).toFloat()
)

fun MovieEntity.toMovie() = Movie(
    id = id,
    posterPath = posterPath,
    title = title,
    voteAverage = rating.toDouble()
)

fun MovieDetailsResponse.toMovie() = Movie(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genres?.mapNotNull { it.id },
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteCount = voteCount,
    voteAverage = voteAverage
)

fun MovieDetailsResponse.toMovieDetailsEntity() = MovieDetailsEntity(
    id ?: 0,
    genres ?: emptyList(),
    overview ?: "",
    popularity ?: (0).toDouble(),
    posterPath ?: "",
    status ?: "",
    title ?: "",
    video ?: false,
    voteAverage ?: (0).toDouble(),
    voteCount ?: 0,
    accountStatesResponse
)

fun MovieDetailsEntity.toMovieDetailsResponse() = MovieDetailsResponse(
    id = id,
    genres = genres,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    status = status,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    accountStatesResponse = accountStatesResponse
)