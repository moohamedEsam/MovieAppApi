package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.*

interface MovieRemoteDataSource {

    suspend fun requestToken(): TokenResponse

    suspend fun login(token: String, username: String, password: String): TokenResponse

    suspend fun createSession(token: String): SessionResponse

    suspend fun getPopularMovies(): MoviesResponse

    suspend fun getTopRatedMovies(): MoviesResponse

    suspend fun getNowPlayingMovies(): MoviesResponse

    suspend fun getUpcomingMovies(): MoviesResponse

    suspend fun getRecommendations(movieId: Int): MoviesResponse

    suspend fun getSimilarMovies(movieId: Int): MoviesResponse

    suspend fun searchAll(query: String): AllSearchResponse

    suspend fun searchMovie(query: String): MoviesResponse

    suspend fun searchTv(query: String): TvShowsResponse

    suspend fun getGenres(): GenreResponse


}