package com.example.movieappapi.domain.utils

sealed class MainFeedMovieList(val tag: String) {
    object Popular : MainFeedMovieList("popular")

    object TopRated : MainFeedMovieList("topRated")


    object NowPlaying : MainFeedMovieList("nowPlaying")

    object Upcoming : MainFeedMovieList("upcoming")

}
