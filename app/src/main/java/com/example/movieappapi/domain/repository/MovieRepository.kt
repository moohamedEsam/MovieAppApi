package com.example.movieappapi.domain.repository

import com.example.movieappapi.domain.model.AllSearchResponse
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.model.TvShowsResponse
import com.example.movieappapi.domain.utils.Resource

interface MovieRepository {
    suspend fun requestToken(): Resource<Boolean>

    suspend fun login(username: String, password: String): Resource<Boolean>

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
}