package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.CreateListResponse
import com.example.movieappapi.domain.model.UserListDetailsResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class CreateListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(name: String, description: String): Resource<CreateListResponse> {
        val response = repository.createList(name, description)
        response.onSuccess {
            repository.insertUserListDetails(
                UserListDetailsResponse(
                    name = name,
                    description = description,
                    items = listOf(),
                    itemCount = 0
                )
            )
        }
        return response
    }

}
