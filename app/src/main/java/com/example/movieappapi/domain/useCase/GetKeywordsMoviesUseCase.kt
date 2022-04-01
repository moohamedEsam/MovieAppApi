package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository

class GetKeywordsMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(keywordId: Int) =
        repository.getKeywordMovies(keywordId)
}