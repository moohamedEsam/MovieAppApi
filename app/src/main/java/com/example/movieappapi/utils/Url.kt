package com.example.movieappapi.utils

object Url {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    const val REQUEST_TOKEN = "${BASE_URL}authentication/token/new"
    const val LOGIN_USER = "${BASE_URL}authentication/token/validate_with_login"
    const val GUEST_LOGIN = "${BASE_URL}authentication/guest_session/new"
    const val ACCOUNT_DETAILS = "${BASE_URL}account"
    const val POPULAR_MOVIES = "${BASE_URL}movie/popular"
    const val TOP_RATED_MOVIES = "${BASE_URL}movie/top_rated"
    const val UPCOMING_MOVIES = "${BASE_URL}movie/upcoming"
    const val NOW_PLAYING_MOVIES = "${BASE_URL}movie/now_playing"
    const val SEARCH_ALL = "${BASE_URL}search/multi"
    const val SEARCH_MOVIE = "${BASE_URL}search/movie"
    const val SEARCH_TV = "${BASE_URL}search/tv"
    fun getUserCreatedListsUrl(accountId: Int) = "${BASE_URL}account/$accountId/lists"
    fun getUserFavouriteMoviesUrl(accountId: Int) = "${BASE_URL}account/$accountId/favourite/movies"
    fun getImageUrl(path: String) = "${BASE_IMAGE_URL}original/$path"
    fun getRecommendations(movieId: Int) = "${BASE_URL}movie/$movieId/recommendations"
    fun getSimilarMovies(movieId: Int) = "${BASE_URL}movie/$movieId/similar"
}