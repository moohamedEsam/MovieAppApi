package com.example.movieappapi.presentation.screen.account

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.useCase.UpdateCachedUser
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.launch

class AccountViewModel(
    private val updateCachedUser: UpdateCachedUser
) : ViewModel() {
    private val _userStatus = mutableStateOf<Resource<Boolean>>(Resource.Initialized())
    val userStatus: State<Resource<Boolean>> = _userStatus
    fun logOut(context: Context) = viewModelScope.launch {
        try {
            updateCachedUser(context, "", "", false)
            _userStatus.value = Resource.Success(true)

        } catch (exception: Exception) {
            Log.e("ProfileViewModel", "logOut: ${exception.message}")
            _userStatus.value = Resource.Error(exception.localizedMessage)
        }
    }
}