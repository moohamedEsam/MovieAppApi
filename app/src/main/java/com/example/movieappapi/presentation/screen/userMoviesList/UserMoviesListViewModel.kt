package com.example.movieappapi.presentation.screen.userMoviesList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetUserMovieListUseCase
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.UserMovieList
import kotlinx.coroutines.launch

class UserMoviesListViewModel(
    private val userMovieListUseCase: GetUserMovieListUseCase
) : ViewModel() {
    private val _movies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val movies: State<Resource<MoviesResponse>> = _movies

    private val _searchMode = mutableStateOf<Boolean>(false)
    val searchMode: State<Boolean> = _searchMode

    private val _searchResult = mutableStateOf<List<Movie>>(emptyList())
    val searchResult: State<List<Movie>> = _searchResult

    fun setMovies(listType: UserMovieList) = viewModelScope.launch {
        _movies.value = userMovieListUseCase(listType)
    }

    fun setSearchResults(query: String) = viewModelScope.launch {
        _searchMode.value = true
        _searchResult.value =
            _movies.value.data?.results?.filter { it.originalTitle?.contains(query, true) ?: false }
                ?: emptyList()
    }

    fun setSearchMode(value: Boolean) {
        _searchMode.value = value
    }
}