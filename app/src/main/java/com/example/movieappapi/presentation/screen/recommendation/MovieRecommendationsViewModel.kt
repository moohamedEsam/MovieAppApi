package com.example.movieappapi.presentation.screen.recommendation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetRecommendationsUseCase
import com.example.movieappapi.domain.useCase.GetSimilarMoviesUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class MovieRecommendationsViewModel(
    private val recommendationsUseCase: GetRecommendationsUseCase,
    private val similarMoviesUseCase: GetSimilarMoviesUseCase
) : ViewModel() {
    private val _recommendations = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val recommendations: State<Resource<MoviesResponse>> = _recommendations

    private val _similarMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val similarMovies: State<Resource<MoviesResponse>> = _similarMovies


    fun setSimilarMovies(movieId: Int) = viewModelScope.launch {
        _similarMovies.value = Resource.Loading()
        _similarMovies.value = similarMoviesUseCase(movieId = movieId, 1)
    }

    fun setRecommendations(movieId: Int) = viewModelScope.launch {
        _recommendations.value = Resource.Loading()
        _recommendations.value = recommendationsUseCase(movieId, 1)
    }
}