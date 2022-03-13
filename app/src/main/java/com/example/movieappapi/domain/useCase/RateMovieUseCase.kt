package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class RateMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: Int, value: Float) = repository.rateMovie(movieId, value)
}