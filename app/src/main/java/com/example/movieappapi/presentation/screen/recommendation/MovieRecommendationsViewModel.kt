package com.example.movieappapi.presentation.screen.recommendation

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
    val recommendations = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())

    val similarMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())


    fun setSimilarMovies(movieId: Int) = viewModelScope.launch {
        var movies = similarMovies.value.data?.results
        similarMovies.value = Resource.Loading(similarMovies.value.data)
        val response = similarMoviesUseCase(movieId)
        movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        response.data?.results =
            movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        similarMovies.value = response
    }

    fun setRecommendations(movieId: Int) = viewModelScope.launch {
        var movies = recommendations.value.data?.results
        recommendations.value = Resource.Loading(recommendations.value.data)
        val response = similarMoviesUseCase(movieId)
        movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        response.data?.results =
            movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        recommendations.value = response
    }
}