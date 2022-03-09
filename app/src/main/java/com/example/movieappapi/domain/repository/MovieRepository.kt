package com.example.movieappapi.domain.repository

import android.content.Context
import com.example.movieappapi.AppData
import com.example.movieappapi.domain.model.AllSearchResponse
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.model.TvShowsResponse
import com.example.movieappapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun requestToken(context: Context): Resource<Boolean>

    suspend fun login(context: Context, username: String, password: String): Resource<Boolean>

    suspend fun createSession(): Resource<Boolean>

    suspend fun getPopularMovies(): Resource<MoviesResponse>

    suspend fun getTopRatedMovies(): Resource<MoviesResponse>

    suspend fun getNowPlayingMovies(): Resource<MoviesResponse>

    suspend fun getUpcomingMovies(): Resource<MoviesResponse>

    suspend fun getRecommendations(movieId: Int): Resource<MoviesResponse>

    suspend fun getSimilarMovies(movieId: Int): Resource<MoviesResponse>

    suspend fun searchAll(query: String): Resource<AllSearchResponse>

    suspend fun searchMovie(query: String): Resource<MoviesResponse>

    suspend fun searchTv(query: String): Resource<TvShowsResponse>

    suspend fun getGenres(): Resource<GenreResponse>

    suspend fun assignCachedToken(context: Context)

    suspend fun updateToken(context: Context, accessToken: String, expiresAt: String)

    suspend fun getToken(context: Context): Resource<Boolean>

    suspend fun getCachedUser(context: Context): Flow<AppData>

    suspend fun updateUser(context: Context, username: String, password: String, loggedIn: Boolean)
}