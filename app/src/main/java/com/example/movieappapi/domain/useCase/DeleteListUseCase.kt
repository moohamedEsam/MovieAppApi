package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class DeleteListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(listId: Int) = repository.deleteList(listId)
}