package com.example.movieappapi.presentation.screen.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.SearchMoviesUseCase
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.presentation.utils.BaseSearchPaginateViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel(), BaseSearchPaginateViewModel<MoviesResponse> {

    override var results: MutableState<Resource<MoviesResponse>> =
        mutableStateOf(Resource.Initialized())
    override var filteredItems: MutableState<MoviesResponse?> = mutableStateOf(null)
    override var searchMode: MutableState<Boolean> = mutableStateOf(false)
    private var query = ""

    override fun setFilteredItems(query: String) {
        viewModelScope.launch {
            searchMode.value = query.isNotEmpty()
            results.value.onSuccess {
                val movies =
                    it.results?.filter { movie -> movie.title?.contains(query, true) ?: false }
                filteredItems.value = MoviesResponse(results = movies)
            }
        }
    }

    fun setMovies(query: String) = viewModelScope.launch {
        if (query.isBlank()) return@launch
        if (!this@SearchViewModel.query.contains(query)) {
            searchMoviesUseCase.reset()
            results.value = searchMoviesUseCase(query)
        } else {
            val response = searchMoviesUseCase(query)
            var movies = results.value.data?.results ?: emptyList()
            movies = movies.plus(response.data?.results ?: emptyList())
            response.data?.results = movies
            results.value = response
        }
        this@SearchViewModel.query = query
        searchMode.value = false
    }

    fun paginateMovies() = setMovies(query)
}