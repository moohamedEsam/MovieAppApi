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
        val response = repository.getToken(context)
        return when (response.data) {
            true -> {
                val result = repository.login(context, username, password)
                if (result.data == true)
                    repository.createSession()
                else
                    return result
            }
            else -> response
        }

    }
}