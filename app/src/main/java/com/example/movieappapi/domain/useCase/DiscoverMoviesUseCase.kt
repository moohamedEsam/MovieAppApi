package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class DiscoverMoviesUseCase(
    private val repository: MovieRepository
) {
    private var page = 0
    private var endReached = false
    suspend operator fun invoke(params: HashMap<String, String>): Resource<MoviesResponse> {
        if (endReached) return Resource.Success(MoviesResponse(results = emptyList()))
        page++
        val response = repository.discoverMovies(params, page)
        response.onSuccess {
            endReached = page >= it.totalPages ?: 0
        }
        return response
    }
}
