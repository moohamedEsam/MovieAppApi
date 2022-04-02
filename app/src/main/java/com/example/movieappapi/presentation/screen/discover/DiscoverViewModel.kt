package com.example.movieappapi.presentation.screen.discover

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.useCase.GetDiscoverMoviesUseCase
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.presentation.utils.BaseSearchPaginateViewModel
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val discoverMoviesUseCase: GetDiscoverMoviesUseCase
) : ViewModel(), BaseSearchPaginateViewModel<MoviesResponse> {

    override var results: MutableState<Resource<MoviesResponse>> =
        mutableStateOf(Resource.Initialized())
    override var filteredItems: MutableState<MoviesResponse?> = mutableStateOf(null)
    override var searchMode: MutableState<Boolean> = mutableStateOf(false)
    val params = mutableStateOf(hashMapOf<String, String>())

    fun setParams(values: HashMap<String, String>) = viewModelScope.launch {
        params.value = values
    }

    fun addFilter(addParam: (HashMap<String, String>) -> HashMap<String, String>) {
        params.value = addParam(params.value)
    }

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

    fun setMovies() = viewModelScope.launch {
        results.value = Resource.Loading(results.value.data)
        val response = discoverMoviesUseCase(params.value)
        response.onSuccess {
            if (results.value.data != null) {
                val movies = results.value.data?.results?.plus(it.results ?: emptyList())
                it.results = movies
            }
            results.value = response
        }
    }
}