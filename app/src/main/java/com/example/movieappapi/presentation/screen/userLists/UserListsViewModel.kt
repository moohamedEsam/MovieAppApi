package com.example.movieappapi.presentation.screen.userLists

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.UserListsResponse
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class UserListsViewModel : ViewModel(

) {
    private val _userLists = mutableStateOf<Resource<UserListsResponse>>(Resource.Initialized())
    val userLists: State<Resource<UserListsResponse>> = _userLists


    init {
        setUserLists()
    }

    private fun setUserLists() = viewModelScope.launch {
        _userLists.value = Resource.Loading()
        //_userLists.value = repository.getUserFavouriteLists()
        if (_userLists.value is Resource.Success)
            Log.d("UserListsViewModel", "setUserLists: ${_userLists.value.data?.userLists?.size}")
    }
}