package com.example.movieappapi.presentation.screen.userLists

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.UserListsResponse
import com.example.movieappapi.domain.useCase.CreateListUseCase
import com.example.movieappapi.domain.useCase.GetAccountStatusUseCase
import com.example.movieappapi.domain.useCase.GetUserCreatedListsUseCase
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.UserStatus
import kotlinx.coroutines.launch

class UserListsViewModel(
    private val accountStatusUseCase: GetAccountStatusUseCase,
    private val userCreatedListsUseCase: GetUserCreatedListsUseCase,
    private val createListUseCase: CreateListUseCase
) : ViewModel() {
    private val _userLists = mutableStateOf<Resource<UserListsResponse>>(Resource.Initialized())
    val userLists: State<Resource<UserListsResponse>> = _userLists

    private val _userStatus: MutableState<UserStatus> = mutableStateOf(UserStatus.LoggedOut)
    val userStatus: State<UserStatus> = _userStatus

    init {
        viewModelScope.launch {
            setUser()
        }
    }

    private suspend fun setUser() {
        _userStatus.value = accountStatusUseCase()
    }

    fun setUserLists() = viewModelScope.launch {
        _userLists.value = Resource.Loading()
        _userLists.value = userCreatedListsUseCase()
    }

    fun createList(name: String, description: String) = viewModelScope.launch {
        val response = createListUseCase(name, description)
        Log.i("UserListsViewModel", "createList: ${response.data?.listId}")
    }
}