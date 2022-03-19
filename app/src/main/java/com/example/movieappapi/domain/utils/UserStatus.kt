package com.example.movieappapi.domain.utils

import com.example.movieappapi.domain.model.AccountDetailsResponse

sealed class UserStatus(val data: AccountDetailsResponse? = null) {
    object Guest : UserStatus()
    object LoggedOut : UserStatus()
    class LoggedIn(data: AccountDetailsResponse?) : UserStatus(data = data)
}
