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
    private var popularPage = 0

    private val _topRatedMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val topRatedMovies: State<Resource<MoviesResponse>> = _topRatedMovies
    private var topPage = 0

    private val _nowPlayingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val nowPlayingMovies: State<Resource<MoviesResponse>> = _nowPlayingMovies
    private var nowPlayingPage = 0

    init {
        setPopularMovies()
        setNowPlayingMovies()
        setTopRatedMovies()
    }

    fun setNowPlayingMovies() = viewModelScope.launch {
        if (nowPlayingPage++ > nowPlayingMovies.value.data?.totalPages ?: 0) return@launch
        var movies = _nowPlayingMovies.value.data?.results
        _nowPlayingMovies.value = Resource.Loading()
        val response = nowPlayingMoviesUseCase(nowPlayingPage)
        if (response is Resource.Success) {
            movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
            response.data?.results = movies
            _nowPlayingMovies.value = response
        }
    }


    fun setTopRatedMovies() = viewModelScope.launch {
        if (topPage++ > topRatedMovies.value.data?.totalPages ?: 0) return@launch
        var movies = _topRatedMovies.value.data?.results
        _topRatedMovies.value = Resource.Loading()
        val response = topRatedUseCase(topPage)
        if (response is Resource.Success) {
            movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
            response.data?.results = movies
            _topRatedMovies.value = response
        }
    }


    fun setPopularMovies() = viewModelScope.launch {
        if (popularPage++ > popularMovies.value.data?.totalPages ?: 0) return@launch
        var movies = _popularMovies.value.data?.results
        _popularMovies.value = Resource.Loading()
        val response = popularMoviesUseCase(popularPage)
        if (response is Resource.Success) {
            movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
            response.data?.results = movies
            _popularMovies.value = response
        }

    }
}