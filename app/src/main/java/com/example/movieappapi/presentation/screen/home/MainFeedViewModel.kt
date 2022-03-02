package com.example.movieappapi.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetNowPlayingMoviesUseCase
import com.example.movieappapi.domain.useCase.GetPopularMoviesUseCase
import com.example.movieappapi.domain.useCase.GetTopRatedUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedUseCase: GetTopRatedUseCase,
    private val nowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,

    ) : ViewModel() {


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

    private fun setNowPlayingMovies() = viewModelScope.launch {
        _nowPlayingMovies.value = Resource.Loading()
        _nowPlayingMovies.value = nowPlayingMoviesUseCase()
    }


    private fun setTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.value = Resource.Loading()
        _topRatedMovies.value = topRatedUseCase()
    }


    private fun setPopularMovies() = viewModelScope.launch {
        _popularMovies.value = Resource.Loading()
        _popularMovies.value = popularMoviesUseCase()
    }
}