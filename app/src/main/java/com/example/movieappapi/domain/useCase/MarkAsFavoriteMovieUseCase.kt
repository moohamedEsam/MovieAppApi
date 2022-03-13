package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class MarkAsFavoriteMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int,
        isFavorite: Boolean = true
    ) =
        repository.markAsFavorite(
            mediaId = movieId,
            mediaType = "movie",
            isFavorite = isFavorite
        )
}