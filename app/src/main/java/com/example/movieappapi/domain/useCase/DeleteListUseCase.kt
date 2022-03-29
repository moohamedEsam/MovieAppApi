package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class DeleteListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(listId: Int): Resource<RateMediaResponse> {
        val response = repository.deleteList(listId)
        response.onSuccess {
            repository.deleteUserListDetails(listId)
        }
        return response
    }
}