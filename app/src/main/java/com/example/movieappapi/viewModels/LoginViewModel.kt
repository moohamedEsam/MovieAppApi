package com.example.movieappapi.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _userState: MutableState<Resource<Unit>> =
        mutableStateOf(Resource.Initialized())
    val userState: State<Resource<Unit>> = _userState

    init {
        _userState.value = if (isUserSignedIn())
            Resource.Success(Unit)
        else
            Resource.Initialized()
    }

    fun validCredentials(credentials: Credentials): Boolean =
        credentials.username.trim().isNotBlank() && credentials.password.trim().isNotBlank()


    fun signIn(credentials: Credentials) = viewModelScope.launch {
        _userState.value = Resource.Loading()
        if (validCredentials(credentials)) {
            _userState.value = repository.signIn(credentials)
        } else
            _userState.value = Resource.Error("not valid username or password")
    }

    fun signInAsGuest() = viewModelScope.launch {
        _userState.value = repository.loginAsGuest()
    }

    fun signInWithGoogle(token: String) = viewModelScope.launch {
        _userState.value = repository.signInWithGoogle(token)
    }

    fun register(credentials: Credentials) = viewModelScope.launch {
        _userState.value = repository.register(credentials = credentials)
    }

    private fun isUserSignedIn() = repository.isUserSignedIn()

    fun reinitializeUserState() {
        _userState.value = Resource.Initialized()
    }
}