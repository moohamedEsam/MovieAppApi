package com.example.movieappapi.presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetMainFeedMoviesUseCase
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class MainFeedViewModel(
    private val nowPlaying: GetMainFeedMoviesUseCase,
    private val topRated: GetMainFeedMoviesUseCase,
    private val upcoming: GetMainFeedMoviesUseCase,
    private val popular: GetMainFeedMoviesUseCase
) : ViewModel() {

    private val _popularMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val popularMovies: State<Resource<MoviesResponse>> = _popularMovies


    private val _upcomingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val upcomingMovies: State<Resource<MoviesResponse>> = _upcomingMovies


    private val _topRatedMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val topRatedMovies: State<Resource<MoviesResponse>> = _topRatedMovies


    private val _nowPlayingMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val nowPlayingMovies: State<Resource<MoviesResponse>> = _nowPlayingMovies


    init {
        setPopularMovies()
        setNowPlayingMovies()
        setTopRatedMovies()
        setUpcomingMovies()
    }

    suspend fun setMovies(
        resource: MutableState<Resource<MoviesResponse>>,
        movieList: MainFeedMovieList
    ) {
        var movies = resource.value.data?.results
        val response = when (movieList) {
            is MainFeedMovieList.TopRated -> topRated(movieList)
            is MainFeedMovieList.NowPlaying -> nowPlaying(movieList)
            is MainFeedMovieList.Upcoming -> upcoming(movieList)
            else -> popular(movieList)
        }
        movies = movies?.plus(response.data?.results ?: emptyList()) ?: response.data?.results
        response.data?.results = movies
        resource.value = response

    }

    fun setUpcomingMovies() = viewModelScope.launch {
        setMovies(_upcomingMovies, MainFeedMovieList.Upcoming)
    }

    fun setNowPlayingMovies() = viewModelScope.launch {
        setMovies(_nowPlayingMovies, MainFeedMovieList.NowPlaying)
    }

    fun setPopularMovies() = viewModelScope.launch {
        setMovies(_popularMovies, MainFeedMovieList.Popular)

    }

    fun setTopRatedMovies() = viewModelScope.launch {
        setMovies(_topRatedMovies, MainFeedMovieList.TopRated)
    }


}