package com.example.movieappapi.presentation.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.useCase.LoginUseCase
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _userState: MutableState<Resource<Unit>> =
        mutableStateOf(Resource.Initialized())
    val userState: State<Resource<Unit>> = _userState

    fun login(username: String, password: String) = viewModelScope.launch {
        _userState.value = Resource.Loading()
        val result = loginUseCase(username.trim(), password.trim())
        if (result is Resource.Success && result.data == true)
            _userState.value = Resource.Success(Unit)
        _userState.value = Resource.Error(result.message)


    }
}