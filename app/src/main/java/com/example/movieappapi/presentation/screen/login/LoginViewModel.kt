package com.example.movieappapi.presentation.screen.login

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.useCase.GetUserUseCase
import com.example.movieappapi.domain.useCase.LoginAsGuestUseCase
import com.example.movieappapi.domain.useCase.LoginUseCase
import com.example.movieappapi.domain.useCase.UpdateCachedUser
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateCachedUser: UpdateCachedUser,
    private val loginAsGuestUseCase: LoginAsGuestUseCase
) : ViewModel() {

    private val _userState: MutableState<Resource<Unit>> =
        mutableStateOf(Resource.Initialized())
    val userState: State<Resource<Unit>> = _userState

    private val _username = mutableStateOf<String>("")
    val username: State<String> = _username

    private val _password = mutableStateOf<String>("")
    val password: State<String> = _password

    fun userAlreadyLoggedIn(context: Context) = viewModelScope.launch {
        _userState.value = Resource.Loading()
        val data = getUserUseCase(context)
        val loggedIn = data.loggedIn
        _userState.value = if (loggedIn) {
            loginUseCase(context, data.username, data.password)
            Resource.Success(Unit)
        } else
            Resource.Error("")
    }

    fun setPassword(value: String) = viewModelScope.launch {
        _password.value = value
    }

    fun setUsername(value: String) = viewModelScope.launch {
        _username.value = value
    }

    fun login(context: Context) = viewModelScope.launch {
        _userState.value = Resource.Loading()
        val result = loginUseCase(context, "mohamedEsam", "0Epn4HAjP176")
        if (result is Resource.Success && result.data == true) {
            _userState.value = Resource.Success(Unit)
            updateCachedUser(context, "mohamedEsam", "0Epn4HAjP176", true)
        } else
            _userState.value = Resource.Error(result.message)
    }

    fun loginAsGuest() = viewModelScope.launch {
        _userState.value = Resource.Loading()
        val result = loginAsGuestUseCase()
        _userState.value = if (result is Resource.Success && result.data == true)
            Resource.Success(Unit)
        else
            Resource.Error(result.message)
    }
}