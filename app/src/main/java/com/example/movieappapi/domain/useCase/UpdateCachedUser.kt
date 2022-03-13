package com.example.movieappapi.domain.useCase

import android.content.Context
import com.example.movieappapi.domain.repository.MovieRepository

class UpdateCachedUser(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        context: Context,
        username: String,
        password: String,
        loggedIn: Boolean
    ) {
        repository.updateUser(context, username, password, loggedIn)
        if (!loggedIn)
            repository.resetRepository()
    }
}