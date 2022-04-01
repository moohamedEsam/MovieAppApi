package com.example.movieappapi.presentation.screen.lists

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.UserListDetailsResponse
import com.example.movieappapi.domain.useCase.GetListDetailsUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class ListViewModel(
    private val listDetailsUseCase: GetListDetailsUseCase
) : ViewModel() {
    private var listId: Int = 0
    private val _list =
        mutableStateOf<Resource<UserListDetailsResponse>>(Resource.Initialized())
    val list: State<Resource<UserListDetailsResponse>> = _list

    private val _searchMode = mutableStateOf<Boolean>(false)
    val searchMode: State<Boolean> = _searchMode

    private val _searchResult = mutableStateOf<List<Movie>>(emptyList())
    val searchResult: State<List<Movie>> = _searchResult

    fun setListId(value: Int) {
        listId = value
    }

    fun getList() = viewModelScope.launch {
        _list.value = Resource.Loading(_list.value.data)
        _list.value = listDetailsUseCase(listId)
    }

    fun setSearchResults(query: String) = viewModelScope.launch {
        _searchMode.value = true
        _searchResult.value =
            _list.value.data?.items?.filter { it.originalTitle?.contains(query, true) ?: false }
                ?: emptyList()
    }

    fun setSearchMode(value: Boolean) {
        _searchMode.value = value
    }

}