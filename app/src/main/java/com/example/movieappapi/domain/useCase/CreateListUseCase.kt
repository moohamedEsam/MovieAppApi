package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class CreateListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(name: String, description: String) =
        repository.createList(name, description)
}