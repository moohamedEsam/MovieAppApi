package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.collectLatest

class AddMovieToListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        movieId: Int,
        listId: Int,
        favorite: Boolean
    ): Resource<RateMediaResponse> {
        val response = repository.addMovieToList(listId, movieId)
        response.onSuccess {
            repository.getUserListDetailsEntity(listId).collectLatest {
                it.onSuccess { userList ->
                    userList.itemCount++
                    userList.items = userList.items?.plus(movieId)
                    userList.favoriteCount += if (favorite)
                        1
                    else 0
                }
                if (response.data != null)
                    repository.updateUserListDetails(it.data!!)
            }
        }
        return response

    }

}
