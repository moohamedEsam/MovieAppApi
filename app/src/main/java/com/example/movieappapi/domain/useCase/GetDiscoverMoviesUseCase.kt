package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.DiscoverType
import com.example.movieappapi.domain.utils.Resource

class GetDiscoverMoviesUseCase(
    private val repository: MovieRepository
) {
    private var page = 0
    private var endReached = false
    private var isLoading = false
    suspend operator fun invoke(id: Int, discoverType: DiscoverType): Resource<MoviesResponse> {
        if (endReached || isLoading) return Resource.Success(MoviesResponse(results = emptyList()))
        page++
        isLoading = true
        val response = repository.getDiscoverMovies(id, discoverType, page = page)
        response.onSuccess {
            endReached = page >= it.totalPages ?: 0
        }
        isLoading = false
        return response
    }
}
