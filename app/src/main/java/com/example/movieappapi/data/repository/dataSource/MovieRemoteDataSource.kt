package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.MoviesResponse

interface MovieRemoteDataSource {

    suspend fun getPopularMovies(): MoviesResponse

    suspend fun discoverMovies(): MoviesResponse

    suspend fun getTopRatedMovies(): MoviesResponse

    suspend fun getNowPlayingMovies(): MoviesResponse

    suspend fun getUserFavoriteMovies(accountId: Int, token: String): MoviesResponse

    suspend fun getUserMovieWatchList(accountId: Int, token: String): MoviesResponse

    suspend fun getUserRatedMovies(accountId: Int, token: String): MoviesResponse

    suspend fun getUpcomingMovies(): MoviesResponse

    suspend fun getRecommendations(movieId: Int): MoviesResponse

    suspend fun getSimilarMovies(movieId: Int): MoviesResponse

    suspend fun searchMovie(query: String): MoviesResponse
}