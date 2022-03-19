package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class DeleteTvRateUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(tvId: Int) = repository.deleteRateTv(tvId)
}