package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class SearchTvUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String) = repository.searchTv(query)
}