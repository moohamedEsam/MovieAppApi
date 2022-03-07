package com.example.movieappapi.domain.useCase

import android.content.Context
import com.example.movieappapi.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first

class GetUserUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(context: Context) = repository.getCachedUser(context).first()
}