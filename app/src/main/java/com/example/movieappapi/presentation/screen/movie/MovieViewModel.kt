package com.example.movieappapi.presentation.screen.movie

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.useCase.GetGenresUseCase
import com.example.movieappapi.domain.useCase.MarkAsFavoriteMovieUseCase
import com.example.movieappapi.domain.useCase.RateMovieUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val genresUseCase: GetGenresUseCase,
    private val rateMovieUseCase: RateMovieUseCase,
    private val markAsFavoriteMovieUseCase: MarkAsFavoriteMovieUseCase
) : ViewModel() {
    private val _genres = mutableStateOf<GenreResponse?>(null)
    private var movie = Movie()


    fun setMovie(value: Movie) = viewModelScope.launch {
        movie = value
    }


    init {
        setGenres()
    }

    private fun setGenres() = viewModelScope.launch {
        _genres.value = genresUseCase().data
    }

    fun getMovieGenres(ids: List<Int>): Flow<List<String>> = flow {
        val genres = mutableListOf<String>()
        _genres.value?.genres?.let {
            for (i in it.indices) {
                if (it[i].id in ids)
                    genres.add(it[i].name ?: "")
            }
        }
        emit(genres.toList())
    }

    fun rate(value: Float) = viewModelScope.launch {
        rateMovieUseCase(movie.id ?: 1, value)
    }

    fun markFavorite() = viewModelScope.launch {
        val response = markAsFavoriteMovieUseCase(movie.id ?: 1)
    }
}