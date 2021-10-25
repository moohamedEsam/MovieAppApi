package com.example.movieappapi.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.RequestTokenResponse
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource

import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _userState: MutableState<Resource<RequestTokenResponse>> =
        mutableStateOf(Resource.Initialized())
    val userState: State<Resource<RequestTokenResponse>> = _userState
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

    fun register(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.themoviedb.org/signup")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun reinitializeUserState() {
        _userState.value = Resource.Initialized()
    }
}