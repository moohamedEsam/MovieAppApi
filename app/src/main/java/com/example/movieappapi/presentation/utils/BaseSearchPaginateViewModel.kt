package com.example.movieappapi.presentation.utils

import androidx.compose.runtime.MutableState
import com.example.movieappapi.domain.utils.Resource

interface BaseSearchPaginateViewModel<Item> {
    var results: MutableState<Resource<Item>>
    var filteredItems: MutableState<Item?>
    var searchMode: MutableState<Boolean>

    fun setFilteredItems(query: String)
}