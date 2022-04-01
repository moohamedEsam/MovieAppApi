package com.example.movieappapi.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetMainFeedMoviesUseCase
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(private val mainFeedMoviesUseCase: GetMainFeedMoviesUseCase) : ViewModel() {

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
        var movies = _nowPlayingMovies.value.data?.results
        _nowPlayingMovies.value = Resource.Loading()
        mainFeedMoviesUseCase(MainFeedMovieList.NowPlaying).let {
            if (it !is Resource.Success) return@let
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _nowPlayingMovies.value = it
        }
    }


    fun setTopRatedMovies() = viewModelScope.launch {
        var movies = _topRatedMovies.value.data?.results
        _topRatedMovies.value = Resource.Loading()
        mainFeedMoviesUseCase(MainFeedMovieList.TopRated).let {
            if (it !is Resource.Success) return@let
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _topRatedMovies.value = it
        }
    }


    fun setPopularMovies() = viewModelScope.launch {
        var movies = _popularMovies.value.data?.results
        _popularMovies.value = Resource.Loading()
        mainFeedMoviesUseCase(MainFeedMovieList.Popular).let {
            if (it !is Resource.Success) return@let
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _popularMovies.value = it
        }

    }
}