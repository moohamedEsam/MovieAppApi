package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Resource<MoviesResponse> {
        val movie = repository.getLatestMovieAdded()
        val calendar = Calendar.getInstance()
        calendar.time = movie?.dateAdded ?: Date()
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        return if (!Date(calendar.timeInMillis).before(Date())) {
            val movies = repository.getLocalMovies()
            Resource.Success(MoviesResponse(page = 1, movies))
        } else {
            val response = repository.getPopularMovies(page)
            if (response is Resource.Success) {
                CoroutineScope(Dispatchers.Default).launch {
                    repository.deleteAllMovies()
                    response.data?.results?.let {
                        repository.insertLocalMovies(it)
                    }
                }
            }
            response
        }

    }
}