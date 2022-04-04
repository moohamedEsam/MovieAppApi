package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class RemoveMovieFromListUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int, listId: Int, favorite: Boolean) = flow {
        val response = repository.removeMovieFromList(listId, movieId)
        response.onSuccess {
            repository.getUserListDetailsEntity(listId).collectLatest {
                it.onSuccess { userList ->
                    userList.itemCount--
                    userList.items = userList.items?.filter { id -> id != movieId }
                    userList.favoriteCount -= if (favorite)
                        1
                    else 0
                }
                if (it.data != null)
                    repository.updateUserListDetails(it.data!!)

                emit(response)
            }
        }

    }

}
