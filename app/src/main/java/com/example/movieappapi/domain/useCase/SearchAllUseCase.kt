package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class SearchAllUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(query: String) = repository.searchAll(query)
}