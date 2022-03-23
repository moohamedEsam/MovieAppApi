package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class GetMainFeedMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieList: MainFeedMovieList, page: Int): Resource<MoviesResponse> {
        val movie = repository.getLatestMovieAdded(movieList.tag)
        val calendar = Calendar.getInstance()
        calendar.time = movie?.dateAdded ?: Date()
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        return if (movie != null && !Date(calendar.timeInMillis).before(Date())) {
            val movies = repository.getLocalMovies(movieList)
            Resource.Success(MoviesResponse(page = 1, movies))
        } else {
            val response = when (movieList) {
                is MainFeedMovieList.Popular -> repository.getPopularMovies(page = page)
                is MainFeedMovieList.TopRated -> repository.getTopRatedMovies(page)
                else -> repository.getNowPlayingMovies(page)
            }
            if (response is Resource.Success) {
                CoroutineScope(Dispatchers.Default).launch {
                    repository.deleteAllMovies(movieList.tag)
                    response.data?.results?.let {
                        repository.insertLocalMovies(it, movieList.tag)
                    }
                }
            }
            response
        }

    }
}