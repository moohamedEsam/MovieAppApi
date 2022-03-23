package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class GetListDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(listId: Int) = repository.getList(listId)
}