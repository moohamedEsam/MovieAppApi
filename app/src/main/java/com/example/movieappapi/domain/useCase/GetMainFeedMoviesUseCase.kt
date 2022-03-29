package com.example.movieappapi.domain.useCase

import android.util.Log
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class GetMainFeedMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieList: MainFeedMovieList, page: Int) = channelFlow {
        repository.getLatestMovieAdded(movieList.tag).collectLatest { movie ->
            val calendar = Calendar.getInstance()
            calendar.time = movie.dateAdded ?: Date()
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            if (!Date(calendar.timeInMillis).before(Date()) && page == 1) {
                repository.getLocalMovies(movieList).collectLatest { movies ->
                    Log.i("GetMainFeedMoviesUseCase", "invoke: in")
                    send(Resource.Success(MoviesResponse(page = page, movies, totalPages = 4)))
                }
            } else {
                val response = when (movieList) {
                    is MainFeedMovieList.Popular -> repository.getPopularMovies(page = page)
                    is MainFeedMovieList.TopRated -> repository.getTopRatedMovies(page)
                    else -> repository.getNowPlayingMovies(page)
                }
                if (response is Resource.Success && page == 1) {
                    repository.deleteAllMovies(movieList.tag)
                    response.data?.results?.let {
                        repository.insertLocalMovies(it, movieList.tag)
                    }
                }
                send(response)
            }
        }

    }
}