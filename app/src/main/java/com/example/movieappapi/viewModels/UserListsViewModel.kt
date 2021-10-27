package com.example.movieappapi.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.UserListResponse
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.launch

class UserListsViewModel(private val repository: Repository) : ViewModel() {
    private val _userLists = mutableStateOf<Resource<UserListResponse>>(Resource.Initialized())
    val userLists: State<Resource<UserListResponse>> = _userLists

    init {
        setUserLists()
    }

    fun setUserLists() = viewModelScope.launch {
        _userLists.value = Resource.Loading()
        _userLists.value = repository.getUserFavouriteLists()
        if (_userLists.value is Resource.Success)
            Log.d("UserListsViewModel", "setUserLists: ${_userLists.value.data?.userLists?.size}")
    }
}