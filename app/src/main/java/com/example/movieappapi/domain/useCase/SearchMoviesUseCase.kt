package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.BasePaginationUseCase
import com.example.movieappapi.domain.utils.Resource

class SearchMoviesUseCase(
    private val repository: MovieRepository
) : BasePaginationUseCase<MoviesResponse> {
    override var page = 1
    override var endReached = false
    suspend operator fun invoke(query: String): Resource<MoviesResponse> {
        if (endReached) return Resource.Success(MoviesResponse())
        val response = request {
            repository.searchMovie(query, page)
        }
        response.onSuccess {
            endReached = page >= it.totalPages ?: 0
        }
        return response
    }
}