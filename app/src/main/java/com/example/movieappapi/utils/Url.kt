package com.example.movieappapi.utils

object Url {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val REQUEST_TOKEN = "${BASE_URL}authentication/token/new"
    const val  LOGIN_USER = "${BASE_URL}authentication/token/validate_with_login"
}