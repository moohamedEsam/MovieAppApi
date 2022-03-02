package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class LoginUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<Boolean> {
        val response = repository.requestToken()
        return when (response.data) {
            true -> {
                val result = repository.login(username, password)
                if (result.data == true)
                    repository.createSession()
                else
                    return result
            }
            else -> response
        }

    }
}