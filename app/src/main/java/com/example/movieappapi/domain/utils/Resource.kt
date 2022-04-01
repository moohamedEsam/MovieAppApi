package com.example.movieappapi.domain.utils

import androidx.compose.runtime.Composable

sealed class Resource<T>(var data: T? = null, var message: String? = null) {

    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Loading<T>(preData: T?) : Resource<T>(data = preData)

    class Initialized<T> : Resource<T>()

    suspend fun onSuccess(perform: suspend (T) -> Unit) {
        if (this is Success && data != null)
            perform(data!!)
    }

    @Composable
    fun OnSuccessComposable(perform: @Composable (T) -> Unit) {
        if (this is Success && data != null)
            perform(data!!)
    }
}


