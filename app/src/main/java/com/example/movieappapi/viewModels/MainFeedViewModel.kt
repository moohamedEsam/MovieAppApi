package com.example.movieappapi.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(private val repository: Repository) : ViewModel() {
    private val _popularMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val popularMovies: State<Resource<MoviesResponse>> = _popularMovies

    init {
        setPopularMovies()
    }

    fun setPopularMovies() = viewModelScope.launch {
        _popularMovies.value = Resource.Loading()
        _popularMovies.value = repository.getPopularMovies()
    }
}