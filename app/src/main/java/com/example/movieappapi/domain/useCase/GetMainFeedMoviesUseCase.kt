package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieListType
import com.example.movieappapi.domain.utils.Resource

class GetMainFeedMoviesUseCase(
    private val repository: MovieRepository
) {
    private var endReached = false
    suspend operator fun invoke(movieListTypeType: MainFeedMovieListType): Resource<MoviesResponse> {
        if (endReached) return Resource.Error("${movieListTypeType.tag} loading")
        val date = repository.getLatestMovieAdded(movieListTypeType.tag)
        return if (date == null)
            repository.getMainFeedMovies(movieListTypeType).also {
                it.onSuccess { movies ->
                    repository.insertLocalMovies(
                        movies = movies.results ?: emptyList(),
                        tag = movieListTypeType.tag
                    )
                }

            }
        else {
            return if (movieListTypeType.page != 1)
                repository.getMainFeedMovies(movieListTypeType).also {
                    it.onSuccess { moviesResponse ->
                        endReached = movieListTypeType.page >= moviesResponse.totalPages ?: 0
                    }

                }
            else
                getLocalMovies(movieListTypeType)
        }
    }


    private suspend fun getLocalMovies(movieListTypeType: MainFeedMovieListType): Resource.Success<MoviesResponse> {
        val movies = repository.getLocalMovies(movieListTypeType)
        return Resource.Success(MoviesResponse(page = 1, movies, totalPages = 4))
    }

}
