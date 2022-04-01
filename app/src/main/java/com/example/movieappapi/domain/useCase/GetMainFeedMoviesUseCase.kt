package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource

class GetMainFeedMoviesUseCase(
    private val repository: MovieRepository
) {
    private var page = 0
    private var endReached = false
    suspend operator fun invoke(movieListType: MainFeedMovieList): Resource<MoviesResponse> {
        if (endReached) return Resource.Success(MoviesResponse(results = emptyList()))
        page++
        val date = repository.getLatestMovieAdded(movieListType.tag)
        return if (date == null)
            getRemoteMovies(movieListType).also {
                it.onSuccess { movies ->
                    repository.insertLocalMovies(
                        movies = movies.results ?: emptyList(),
                        tag = movieListType.tag
                    )

                }
            }
        else {
            return if (page != 1)
                getRemoteMovies(movieListType).also {
                    it.onSuccess { moviesResponse ->
                        endReached = page >= moviesResponse.totalPages ?: 0
                    }
                }
            else
                getLocalMovies(movieListType)
        }
    }


    private suspend fun getLocalMovies(movieListType: MainFeedMovieList): Resource.Success<MoviesResponse> {
        val movies = repository.getLocalMovies(movieListType)
        return Resource.Success(MoviesResponse(page = 1, movies, totalPages = 4))
    }


    private suspend fun getRemoteMovies(movieListType: MainFeedMovieList) =
        when (movieListType) {
            is MainFeedMovieList.Popular -> repository.getPopularMovies(page = page)
            is MainFeedMovieList.TopRated -> repository.getTopRatedMovies(page)
            else -> repository.getNowPlayingMovies(page)
        }
}
