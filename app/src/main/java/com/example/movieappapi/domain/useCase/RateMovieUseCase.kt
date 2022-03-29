package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class RateMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movie: MovieDetailsResponse,
        value: Float
    ): Resource<RateMediaResponse> {
        val response = repository.rateMovie(movie.id ?: 0, value)
        response.onSuccess {
            repository.updateLocalMovieDetails(movie)
        }
        return response
    }
}