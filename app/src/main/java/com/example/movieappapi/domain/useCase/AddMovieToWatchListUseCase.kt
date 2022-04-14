package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class AddMovieToWatchListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.addToWatchList(movieId, "movie")
}
