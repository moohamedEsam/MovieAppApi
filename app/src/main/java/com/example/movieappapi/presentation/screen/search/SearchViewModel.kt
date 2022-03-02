package com.example.movieappapi.presentation.screen.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.AllSearchResponse
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.model.Result
import com.example.movieappapi.domain.useCase.GetGenresUseCase
import com.example.movieappapi.domain.useCase.SearchAllUseCase
import com.example.movieappapi.domain.useCase.SearchMoviesUseCase
import com.example.movieappapi.domain.useCase.SearchTvUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val genresUseCase: GetGenresUseCase,
    private val searchAllUseCase: SearchAllUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvUseCase: SearchTvUseCase
) : ViewModel() {
    private val _filterMode = mutableStateOf<Boolean>(false)
    val searchMode: State<Boolean> = _filterMode

    private val _searchResults = mutableStateOf<Resource<AllSearchResponse>>(Resource.Initialized())
    val searchResults: State<Resource<AllSearchResponse>> = _searchResults

    private var lastQuery = ""

    private val _genres = mutableStateOf<GenreResponse?>(null)
    val genres: State<GenreResponse?> = _genres

    private val _sortByFilter = mutableStateOf<String>("default")
    val sortByFilter: State<String> = _sortByFilter

    private val _typeFilter = mutableStateOf<String>("all")
    val typeFilter: State<String> = _typeFilter

    private val _genreFilter = mutableStateOf<String>("all")
    val genreFilter: State<String> = _genreFilter

    private val _filteredItems = mutableStateOf<List<Result>>(emptyList())
    val filteredItems: State<List<Result>> = _filteredItems

    fun setGenreFilter(value: String) = viewModelScope.launch {
        _genreFilter.value = value
    }

    fun setTypeFilter(value: String) = viewModelScope.launch {
        _typeFilter.value = value
    }

    fun setSortByFilter(value: String) = viewModelScope.launch {
        _sortByFilter.value = value
    }

    fun setGenres() = viewModelScope.launch {
        _genres.value = genresUseCase().data
    }

    fun setSearchResults(query: String) = viewModelScope.launch {
        _filterMode.value = false
        _searchResults.value = Resource.Loading()
        if (lastQuery.contains(query)) {
            setSearchResults(filterResultsByQuery(query) ?: emptyList())
        } else
            _searchResults.value = searchAllUseCase(query.trim())

    }

    private fun filterResultsByQuery(query: String) =
        _searchResults.value.data?.results?.filter {
            it.originalName?.contains(query) == true
                    || it.originalTitle?.contains(query) == true
        }

    private fun setSearchResults(results: List<Result>) {
        _filterMode.value = false
        _searchResults.value = Resource.Loading()
        _searchResults.value.data?.results = results
    }

    fun filterResults(dispatcher: CoroutineDispatcher = Dispatchers.Default) =
        viewModelScope.launch(context = dispatcher) {
            _filterMode.value = true
            _filteredItems.value = _searchResults.value.data?.results ?: emptyList()
            filterByType()
            filterByGenre()
            sortResults()
        }

    private fun sortResults() {
        if (_sortByFilter.value != "all")
            _filteredItems.value = when (_sortByFilter.value) {
                "popularity" -> _filteredItems.value.sortedBy { it.popularity }
                "vote count" -> _filteredItems.value.sortedBy { it.voteCount }
                "vote average" -> _filteredItems.value.sortedBy { it.voteAverage }
                else -> _filteredItems.value
            }
    }

    private fun filterByGenre() {
        if (_genreFilter.value != "all") {
            val genreId = _genres.value?.genres?.find { it.name == _genreFilter.value }?.id ?: 0
            _filteredItems.value =
                _filteredItems.value.filter { it.genreIds?.any { id -> id == genreId } == true }
        }
    }

    private fun filterByType() {
        if (_typeFilter.value != "all")
            _filteredItems.value =
                _filteredItems.value.filter { it.mediaType == _typeFilter.value }
    }
}