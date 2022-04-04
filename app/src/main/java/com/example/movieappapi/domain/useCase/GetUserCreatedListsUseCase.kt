package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class GetUserCreatedListsUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke() = repository.getUserLists()
}