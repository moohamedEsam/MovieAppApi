package com.example.movieappapi.utils

sealed class Resource<T>(var data: T? = null, var message: String? = null) {
    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
    class Initialized<T> : Resource<T>()
}
