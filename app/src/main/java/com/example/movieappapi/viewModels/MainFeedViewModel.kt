package com.example.movieappapi.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.Movie
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(private val repository: Repository) : ViewModel() {


    private val _popularMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val popularMovies: State<Resource<MoviesResponse>> = _popularMovies

    private val _topRatedMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val topRatedMovies: State<Resource<MoviesResponse>> = _topRatedMovies

    private val _nowPlayingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val nowPlayingMovies: State<Resource<MoviesResponse>> = _nowPlayingMovies

    init {
        setPopularMovies()
        setNowPlayingMovies()
        setTopRatedMovies()
    }

    fun setNowPlayingMovies() = viewModelScope.launch {
    	_nowPlayingMovies.value = Resource.Loading()
        _nowPlayingMovies.value = repository.getNowPlayingMovies()
    }


    fun setTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.value = Resource.Loading()
        _topRatedMovies.value = repository.getTopRatedMovies()
    }



    fun setPopularMovies() = viewModelScope.launch {
        _popularMovies.value = Resource.Loading()
        _popularMovies.value = repository.getPopularMovies()
    }
}