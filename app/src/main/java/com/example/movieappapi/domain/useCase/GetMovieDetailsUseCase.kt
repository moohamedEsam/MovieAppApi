package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = channelFlow {
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
}