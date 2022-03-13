package com.example.movieappapi.domain.useCase

import android.content.Context
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class LoginUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        context: Context,
        username: String,
        password: String
    ): Resource<Boolean> {
        val response = repository.getSession(context, username, password)
        return when (response.data) {
            true -> {
                repository.getAccountDetails()
                response
            }
            else -> login(context, username, password)
        }

    }

    suspend fun login(context: Context, username: String, password: String): Resource<Boolean> {
        val response = repository.requestToken(context)
        val result = repository.login(context, username, password)
        return if (result.data == true) {
            val sessionResponse = repository.createSession(context)
            if (sessionResponse.data == true)
                repository.getAccountDetails()
            sessionResponse
        } else
            result
    }
}