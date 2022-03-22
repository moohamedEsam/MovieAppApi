package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Resource<MovieDetailsResponse> {
        val response = repository.getMovieDetails(movieId)
        if (response is Resource.Success && response.data != null) {
            repository.insertLocalMovieDetails(response.data!!)
        }
        return response
    }
}