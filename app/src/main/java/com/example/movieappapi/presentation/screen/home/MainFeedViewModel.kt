package com.example.movieappapi.presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetMainFeedMoviesUseCase
import com.example.movieappapi.domain.utils.MainFeedMovieListType
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(
    private val mainFeedMoviesUseCase: GetMainFeedMoviesUseCase
) : ViewModel() {

    val popularMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())

    val upcomingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())

    val topRatedMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())

    val nowPlayingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())

    private val topRatedMovieList = MainFeedMovieListType.TopRated
    private val nowPlayingMovieList = MainFeedMovieListType.NowPlaying
    private val popularMovieList = MainFeedMovieListType.Popular
    private val upcomingMovieList = MainFeedMovieListType.Upcoming

    init {
        setPopularMovies()
        setNowPlayingMovies()
        setTopRatedMovies()
        setUpcomingMovies()
    }

    private fun setMovies(
        resource: MutableState<Resource<MoviesResponse>>,
        movieListType: MainFeedMovieListType
    ) = viewModelScope.launch {
        var movies = resource.value.data?.results
        val response = mainFeedMoviesUseCase(movieListType)
        if (response is Resource.Success)
            movieListType.page++
        movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        response.data?.results = movies
        resource.value = response

    }

    fun setUpcomingMovies() = viewModelScope.launch {
        setMovies(upcomingMovies, upcomingMovieList)
    }

    fun setNowPlayingMovies() = viewModelScope.launch {
        setMovies(nowPlayingMovies, nowPlayingMovieList)
    }

    fun setPopularMovies() = viewModelScope.launch {
        setMovies(popularMovies, popularMovieList)
    }

    fun setTopRatedMovies() = viewModelScope.launch {
        setMovies(topRatedMovies, topRatedMovieList)
    }


}