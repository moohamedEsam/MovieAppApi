package com.example.movieappapi.presentation.screen.account

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.useCase.GetAccountStatusUseCase
import com.example.movieappapi.domain.useCase.UpdateCachedUser
import com.example.movieappapi.domain.utils.UserStatus
import kotlinx.coroutines.launch

class AccountViewModel(
    private val updateCachedUser: UpdateCachedUser,
    private val accountStatus: GetAccountStatusUseCase
) : ViewModel() {
    private val _userStatus = mutableStateOf<UserStatus>(
        UserStatus.Guest
    )

    init {
        viewModelScope.launch {
            _userStatus.value = accountStatus()
        }
    }

    val userStatus: State<UserStatus> = _userStatus
    fun logOut(context: Context) = viewModelScope.launch {
        try {
            updateCachedUser(context, "", "", false)
            _userStatus.value = UserStatus.LoggedOut
        } catch (exception: Exception) {
            Log.e("ProfileViewModel", "logOut: ${exception.message}")
        }
    }
}