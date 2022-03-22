package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        userEntity: UserEntity
    ): Resource<Boolean> {
        val response = repository.getSession(userEntity)
        return when (response.data) {
            true -> {
                repository.getAccountDetails()
                response
            }
            else -> login(userEntity)
        }

    }

    suspend fun login(userEntity: UserEntity): Resource<Boolean> {
        val response = repository.requestToken()
        val result = repository.login(userEntity)
        return if (result.data == true) {
            val sessionResponse = repository.createSession()
            CoroutineScope(Dispatchers.Default).launch {
                if (sessionResponse.data == false) return@launch
                repository.updateUser(userEntity)
                repository.updateSession()
                repository.getAccountDetails()
            }
            sessionResponse
        } else
            result
    }
}