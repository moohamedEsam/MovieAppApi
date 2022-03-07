package com.example.movieappapi.domain.useCase

import android.content.Context
import com.example.movieappapi.CachedUser
import com.example.movieappapi.domain.repository.MovieRepository

class UpdateCachedUser(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(context: Context, user: CachedUser) {
        repository.updateUser(context, user)
    }
}