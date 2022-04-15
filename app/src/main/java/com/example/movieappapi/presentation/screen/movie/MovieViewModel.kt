package com.example.movieappapi.presentation.screen.movie

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.useCase.*
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieViewModel(
    private val genresUseCase: GetGenresUseCase,
    private val rateMovieUseCase: RateMovieUseCase,
    private val markAsFavoriteMovieUseCase: MarkAsFavoriteMovieUseCase,
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val deleteMovieRateUseCase: DeleteMovieRateUseCase,
    private val addMovieToListUseCase: AddMovieToWatchListUseCase,
    private val userCreatedListsUseCase: GetUserCreatedListsUseCase
) : ViewModel() {
    val movie = mutableStateOf<Resource<MovieDetailsResponse>>(Resource.Initialized())
    val favoriteResponse = mutableStateOf<Resource<RateMediaResponse>>(Resource.Initialized())
    val rateResponse = mutableStateOf<Resource<RateMediaResponse>>(Resource.Initialized())
    val watchlistResponse = mutableStateOf<Resource<RateMediaResponse>>(Resource.Initialized())

    fun setMovie(movieId: Int) = viewModelScope.launch {
        movie.value = Resource.Loading(movie.value.data)
        movie.value = movieDetailsUseCase(movieId)
    }


    fun markAsFavorite() = viewModelScope.launch {
        movie.value.onSuccess {
            favoriteResponse.value = markAsFavoriteMovieUseCase(
                it.id ?: 0,
                it.accountStatesResponse?.favorite?.not() ?: true
            )

        }
    }

    fun rateMovie(value: Float) = viewModelScope.launch {
        movie.value.onSuccess {
            rateResponse.value = rateMovieUseCase(it.id ?: 0, value)

        }
    }

    fun removeRate() = viewModelScope.launch {
        val accountStates = movie.value.data?.accountStatesResponse
        accountStates?.rated?.isRated = false
        accountStates?.rated?.value = 0f
        movie.value.onSuccess {
            it.accountStatesResponse = accountStates
            rateResponse.value = deleteMovieRateUseCase(it)
        }
    }

    fun toggleAddToList() = viewModelScope.launch {
        movie.value.onSuccess {
            watchlistResponse.value = addMovieToListUseCase(
                it.id ?: 0,
                it.accountStatesResponse?.watchlist?.not() ?: false
            )

        }
    }

    fun getUserLists() = channelFlow {
        userCreatedListsUseCase().collectLatest {
            if (it !is Resource.Success || it.data == null) return@collectLatest
            send(it.data!!)
        }
    }
}