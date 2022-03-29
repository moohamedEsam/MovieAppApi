package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class MarkAsFavoriteMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movie: MovieDetailsResponse,
        isFavorite: Boolean = true
    ): Resource<RateMediaResponse> {
        val response = repository.markAsFavorite(
            mediaId = movie.id ?: 0,
            mediaType = "movie",
            isFavorite = isFavorite
        )
        response.onSuccess {
            repository.updateLocalMovieDetails(movie)
        }
        return response
    }
}