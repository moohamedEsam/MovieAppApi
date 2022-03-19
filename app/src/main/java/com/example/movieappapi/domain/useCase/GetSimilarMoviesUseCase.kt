package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class GetSimilarMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int) =
        repository.getSimilarMovies(movieId, page)
}