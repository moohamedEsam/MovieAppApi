package com.example.movieappapi.domain.utils

sealed class MainFeedMovieListType(val tag: String, var page: Int = 1) {
    object Popular : MainFeedMovieListType("popular")

    object TopRated : MainFeedMovieListType("top_rated")

    object NowPlaying : MainFeedMovieListType("now_playing")

    object Upcoming : MainFeedMovieListType("upcoming")

}
