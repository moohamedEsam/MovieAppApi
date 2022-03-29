package com.example.movieappapi.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetMainFeedMoviesUseCase
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFeedViewModel(private val mainFeedMoviesUseCase: GetMainFeedMoviesUseCase) : ViewModel() {

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
        mainFeedMoviesUseCase(MainFeedMovieList.NowPlaying, nowPlayingPage).collectLatest {
            if (it !is Resource.Success) return@collectLatest
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _nowPlayingMovies.value = it
        }
    }


    fun setTopRatedMovies() = viewModelScope.launch {
        if (topPage++ > topRatedMovies.value.data?.totalPages ?: 0) return@launch
        var movies = _topRatedMovies.value.data?.results
        _topRatedMovies.value = Resource.Loading()
        mainFeedMoviesUseCase(MainFeedMovieList.TopRated, topPage).collectLatest {
            if (it !is Resource.Success) return@collectLatest
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _topRatedMovies.value = it
        }
    }


    fun setPopularMovies() = viewModelScope.launch {
        if (popularPage++ > popularMovies.value.data?.totalPages ?: 0) return@launch
        var movies = _popularMovies.value.data?.results
        _popularMovies.value = Resource.Loading()
        mainFeedMoviesUseCase(MainFeedMovieList.Popular, popularPage).collectLatest {
            if (it !is Resource.Success) return@collectLatest
            movies = movies?.plus(it.data?.results ?: emptyList()) ?: it.data?.results
            it.data?.results = movies
            _popularMovies.value = it
        }

    }
}