package com.example.movieappapi.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.launch

class MovieRecommendationsViewModel(private val repository: Repository) : ViewModel() {
    private val _recommendations = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val recommendations: State<Resource<MoviesResponse>> = _recommendations

    private val _similarMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val similarMovies: State<Resource<MoviesResponse>> = _similarMovies


    fun setSimilarMovies(movieId: Int) = viewModelScope.launch {
        _similarMovies.value = Resource.Loading()
        _similarMovies.value = repository.getSimilarMovies(movieId = movieId)
    }

    fun setRecommendations(movieId: Int) = viewModelScope.launch {
        _recommendations.value = Resource.Loading()
        _recommendations.value = repository.getRecommendations(movieId)
    }
}