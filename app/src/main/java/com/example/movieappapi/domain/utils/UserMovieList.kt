package com.example.movieappapi.domain.utils

sealed class UserMovieList {
    object FavoriteMovies : UserMovieList()

    object RatedMovies : UserMovieList()

    object MoviesWatchlist : UserMovieList()

}

sealed class UserTvList {
    object FavoriteTv : UserTvList()

    object RatedTv : UserTvList()

    object TvWatchlist : UserTvList()

}

