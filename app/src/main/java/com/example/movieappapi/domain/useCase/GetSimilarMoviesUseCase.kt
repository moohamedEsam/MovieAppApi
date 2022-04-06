package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.BasePaginationUseCase
import com.example.movieappapi.domain.utils.Resource

class GetSimilarMoviesUseCase(
    private val repository: MovieRepository
) : BasePaginationUseCase<MoviesResponse> {
    override var page: Int = 1
    override var endReached: Boolean = false

    suspend operator fun invoke(movieId: Int): Resource<MoviesResponse> {
        if (endReached) return Resource.Success(MoviesResponse())
        val response = request {
            repository.getSimilarMovies(movieId, page)
        }
        if (response is Resource.Success)
            page++
        return response
    }
}