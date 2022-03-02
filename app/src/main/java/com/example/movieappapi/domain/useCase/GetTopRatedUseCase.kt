package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class GetTopRatedUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke() = repository.getTopRatedMovies()
}