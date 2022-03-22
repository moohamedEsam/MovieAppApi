package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.domain.repository.MovieRepository

class UpdateCachedUser(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        userEntity: UserEntity
    ) {
        repository.updateUser(userEntity)
        if (!userEntity.loggedIn)
            repository.resetRepository()
    }
}