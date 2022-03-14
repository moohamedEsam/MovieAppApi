package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.UserMovieList

class GetUserMovieListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(list: UserMovieList) = when (list) {
        is UserMovieList.FavoriteMovies -> repository.getUserFavoriteMovies()
        is UserMovieList.MoviesWatchlist -> repository.getUserMovieWatchList()
        else -> repository.getUserRatedMovies()
    }
}