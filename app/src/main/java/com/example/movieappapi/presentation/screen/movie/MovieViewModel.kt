package com.example.movieappapi.presentation.screen.movie

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.useCase.*
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val genresUseCase: GetGenresUseCase,
    private val rateMovieUseCase: RateMovieUseCase,
    private val markAsFavoriteMovieUseCase: MarkAsFavoriteMovieUseCase,
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val deleteMovieRateUseCase: DeleteMovieRateUseCase
) : ViewModel() {
    private val _genres = mutableStateOf<GenreResponse?>(null)

    private val _movie = mutableStateOf<Resource<MovieDetailsResponse>>(Resource.Initialized())
    val movie: State<Resource<MovieDetailsResponse>> = _movie


    fun setMovie(movieId: Int) = viewModelScope.launch {
        _movie.value = Resource.Loading()
        _movie.value = movieDetailsUseCase(movieId)
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

    fun markAsFavorite() = viewModelScope.launch {
        val accountStates = _movie.value.data?.accountStatesResponse
        accountStates?.favorite = accountStates?.favorite?.not()
        _movie.value.onSuccess {
            it.accountStatesResponse = accountStates
            markAsFavoriteMovieUseCase(
                it.id ?: 1,
                accountStates?.favorite ?: true
            )
        }
    }

    fun rateMovie(value: Float) = viewModelScope.launch {
        _movie.value.onSuccess {
            rateMovieUseCase(it.id ?: 1, value)
        }
    }

    fun removeRate() = viewModelScope.launch {
        _movie.value.onSuccess {
            deleteMovieRateUseCase(it.id ?: 1)
        }
    }
}