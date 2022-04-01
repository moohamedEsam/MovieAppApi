package com.example.movieappapi.presentation.screen.keywords

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetKeywordsMoviesUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class KeywordsViewModel(
    private val keywordsMoviesUseCase: GetKeywordsMoviesUseCase
) : ViewModel() {
    private var keywordId = 0
    private val _movies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    private val _filteredMovies = mutableStateOf<Resource<MoviesResponse>>(Resource.Initialized())
    val filteredMovies: State<Resource<MoviesResponse>> = _filteredMovies

    fun setFilteredMovies(query: String) = viewModelScope.launch {
        val movies =
            _movies.value.data?.results?.filter { it.title?.contains(query, true) ?: false }
        _filteredMovies.value = Resource.Success(MoviesResponse(results = movies))
    }

    fun setMovies() = viewModelScope.launch {
        val response = keywordsMoviesUseCase(keywordId)
        _movies.value = response
        _filteredMovies.value = response
    }

    fun setKeywordId(value: Int) {
        keywordId = value
    }
}