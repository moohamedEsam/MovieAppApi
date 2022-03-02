package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String) = repository.searchMovie(query)
}