package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class MarkAsFavoriteMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int,
        isFavorite: Boolean = true
    ): Resource<RateMediaResponse> = repository.markAsFavorite(
        mediaId = movieId,
        mediaType = "movie",
        isFavorite = isFavorite
    )

}