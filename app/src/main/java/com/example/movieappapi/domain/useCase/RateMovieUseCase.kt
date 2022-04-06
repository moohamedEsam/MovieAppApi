package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class RateMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int,
        value: Float
    ): Resource<RateMediaResponse> = repository.rateMovie(movieId, value)


}