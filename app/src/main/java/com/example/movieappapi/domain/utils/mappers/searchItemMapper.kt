package com.example.movieappapi.domain.utils.mappers

import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.SearchResult

fun SearchResult.toMovie() = Movie(
    adult,
    backdropPath,
    genreIds,
    id,
    originalLanguage,
    originalTitle,
    overview,
    popularity?.toDouble(),
    posterPath, releaseDate, title, video,
    voteAverage?.toDouble(),
    voteCount?.toInt()
)