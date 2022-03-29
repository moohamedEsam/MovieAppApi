package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class DeleteMovieRateUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: MovieDetailsResponse): Resource<RateMediaResponse> {
        val response = repository.deleteRateMovie(movie.id ?: 0)
        response.onSuccess {
            repository.updateLocalMovieDetails(movie)
        }
        return response
    }
}