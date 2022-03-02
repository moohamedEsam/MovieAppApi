package com.example.movieappapi.data.repository.dataSourceImpl

import com.example.movieappapi.data.repository.dataSource.MovieRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieRemoteDataSourceImpl(private val client: HttpClient) : MovieRemoteDataSource {
    override suspend fun requestToken(): TokenResponse = client.get(Url.REQUEST_TOKEN)

    override suspend fun login(token: String, username: String, password: String): TokenResponse =
        client.get(Url.LOGIN_USER) {
            val map = mapOf(
                "request_token" to token,
                "username" to username,
                "password" to password
            )
            body = Json.encodeToString(map)
        }

    override suspend fun createSession(token: String): SessionResponse =
        client.get(Url.CREATE_SESSION) {
            url {
                parameter("request_token", token)
            }
        }

    override suspend fun getPopularMovies(): MoviesResponse = client.get(Url.POPULAR_MOVIES)

    override suspend fun getTopRatedMovies(): MoviesResponse = client.get(Url.TOP_RATED_MOVIES)

    override suspend fun getNowPlayingMovies(): MoviesResponse = client.get(Url.NOW_PLAYING_MOVIES)

    override suspend fun getUpcomingMovies(): MoviesResponse = client.get(Url.UPCOMING_MOVIES)

    override suspend fun getRecommendations(movieId: Int): MoviesResponse =
        client.get(Url.getRecommendations(movieId))

    override suspend fun getSimilarMovies(movieId: Int): MoviesResponse =
        client.get(Url.getSimilarMovies(movieId))

    override suspend fun searchAll(query: String): AllSearchResponse = client.get(Url.SEARCH_ALL) {
        url {
            parameter("query", query)
        }
    }

    override suspend fun searchMovie(query: String): MoviesResponse = client.get(Url.SEARCH_MOVIE) {
        url {
            parameter("query", query)
        }
    }

    override suspend fun searchTv(query: String): TvShowsResponse = client.get(Url.SEARCH_TV) {
        url {
            parameter("query", query)
        }
    }

    override suspend fun getGenres(): GenreResponse = client.get(Url.GENRES)
}