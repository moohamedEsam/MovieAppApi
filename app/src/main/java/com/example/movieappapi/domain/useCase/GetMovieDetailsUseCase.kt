package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int) = channelFlow {
        getFromRemote(movieId).collectLatest {
            send(it)
        }
    }

    private fun getFromRemote(movieId: Int) = flow {

        val response = repository.getMovieDetails(movieId)
        emit(response)
    }

}