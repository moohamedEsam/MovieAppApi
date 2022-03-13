package com.example.movieappapi.domain.repository

import android.content.Context
import com.example.movieappapi.AppData
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun isCurrentUserGuest(): Boolean

    suspend fun requestToken(context: Context): Resource<Boolean>

    suspend fun login(context: Context, username: String, password: String): Resource<Boolean>

    suspend fun getAccountDetails()

    suspend fun resetRepository()

    suspend fun createSession(context: Context): Resource<Boolean>

    suspend fun createGuestSession(): Resource<Boolean>

    suspend fun getPopularMovies(): Resource<MoviesResponse>

    suspend fun getTopRatedMovies(): Resource<MoviesResponse>

    suspend fun getNowPlayingMovies(): Resource<MoviesResponse>

    suspend fun getUpcomingMovies(): Resource<MoviesResponse>

    suspend fun getRecommendations(movieId: Int): Resource<MoviesResponse>

    suspend fun getSimilarMovies(movieId: Int): Resource<MoviesResponse>

    suspend fun discoverMovies(): Resource<MoviesResponse>

    suspend fun getUserFavoriteMovies(): Resource<MoviesResponse>

    suspend fun getUserMovieWatchList(): Resource<MoviesResponse>

    suspend fun getUserRatedMovies(): Resource<MoviesResponse>


    suspend fun searchAll(query: String): Resource<AllSearchResponse>

    suspend fun searchMovie(query: String): Resource<MoviesResponse>

    suspend fun searchTv(query: String): Resource<TvShowsResponse>

    suspend fun getGenres(): Resource<GenreResponse>

    suspend fun getUserCreatedList(): Resource<UserListsResponse>

    suspend fun markAsFavorite(
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean = true
    ): Resource<RateMediaResponse>

    suspend fun addToWatchList(
        mediaId: Int,
        mediaType: String
    ): Resource<RateMediaResponse>

    suspend fun rateMovie(
        movieId: Int,
        value: Float
    ): Resource<RateMediaResponse>

    suspend fun rateTv(
        tvId: Int,
        value: Float
    ): Resource<RateMediaResponse>

    suspend fun getPopularTv(): Resource<TvShowsResponse>

    suspend fun getUserFavoriteTv(): Resource<TvShowsResponse>

    suspend fun getUserRatedTv(): Resource<TvShowsResponse>

    suspend fun getUserRatedTvEpisodes(): Resource<TvShowsResponse>

    suspend fun getUserTvWatchList(): Resource<TvShowsResponse>

    suspend fun discoverTv(): Resource<TvShowsResponse>

    suspend fun assignCachedSession(context: Context)

    suspend fun updateSession(context: Context, sessionId: String, expiresAt: String)

    suspend fun getSession(context: Context, username: String, password: String): Resource<Boolean>

    suspend fun getCachedUser(context: Context): Flow<AppData>

    suspend fun updateUser(context: Context, username: String, password: String, loggedIn: Boolean)

    suspend fun getSession(): SessionResponse
}