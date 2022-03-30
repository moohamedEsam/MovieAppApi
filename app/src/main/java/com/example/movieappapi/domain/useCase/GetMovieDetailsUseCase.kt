package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = channelFlow {
        getFromRemote(movieId)
    }

    private suspend fun ProducerScope<Resource<MovieDetailsResponse>>.getFromLocal(
        movieId: Int
    ) {
        repository.getLocalMovieDetails(movieId).collectLatest { movie ->
            if (movie != null)
                send(Resource.Success(movie))
            else {
                val response = repository.getMovieDetails(movieId)
                if (response is Resource.Success && response.data != null) {
                    repository.insertLocalMovieDetails(response.data!!)
                }
                send(response)
            }

        }
    }

    private suspend fun ProducerScope<Resource<MovieDetailsResponse>>.getFromRemote(
        movieId: Int
    ) {
        val response = repository.getMovieDetails(movieId)
        send(response)
    }

}