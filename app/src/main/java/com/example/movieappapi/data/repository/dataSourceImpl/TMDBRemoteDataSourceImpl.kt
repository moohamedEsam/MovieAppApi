package com.example.movieappapi.data.repository.dataSourceImpl

import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class TMDBRemoteDataSourceImpl(private val client: HttpClient) : TMDBRemoteDataSource {

    override suspend fun getPopularTv(): TvShowsResponse = client.get(Url.POPULAR_Tv)


    override suspend fun getAccountDetails(token: String): AccountDetailsResponse =
        client.get(Url.ACCOUNT_DETAILS) {
            parameter("session_id", token)
        }

    override suspend fun createGuestSession(): GuestSessionResponse = client.post(Url.GUEST_LOGIN)

    override suspend fun discoverMovies(): MoviesResponse = client.get(Url.DISCOVER_MOVIES)

    override suspend fun getUserCreatedList(accountId: Int, token: String): UserListsResponse =
        client.get(Url.getUserLists(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserFavoriteMovies(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserFavoriteMovies(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserFavoriteTv(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserFavoriteTv(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserMovieWatchList(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserWatchListMovies(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserRatedMovies(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserRatedMovies(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun markAsFavorite(
        mediaId: Int,
        accountId: Int,
        mediaType: String
    ): RateMediaResponse = client.post(Url.markAsFavorite(accountId)) {
        parameter("media_type", mediaType)
        parameter("media_id", mediaId)
    }

    override suspend fun addToWatchList(
        mediaId: Int,
        accountId: Int,
        mediaType: String
    ): RateMediaResponse = client.post(Url.addWatchList(accountId)) {
        parameter("media_type", mediaType)
        parameter("media_id", mediaId)
    }

    override suspend fun getUserRatedTv(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserRatedTv(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserRatedTvEpisodes(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserRatedTvEpisodes(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun getUserTvWatchList(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserWatchListTv(accountId)) {
            parameter("session_id", token)
        }

    override suspend fun rateMovie(
        movieId: Int,
        token: String
    ): RateMediaResponse = client.post(Url.rateMovie(movieId)) {
        parameter("session_id", token)
    }

    override suspend fun rateTv(tvId: Int, token: String): RateMediaResponse =
        client.post(Url.rateTv(tvId)) {
            parameter("session_id", token)
        }

    override suspend fun discoverTv(): TvShowsResponse = client.get(Url.DISCOVER_MOVIES)

    override suspend fun requestToken(): TokenResponse =
        client.get(Url.REQUEST_TOKEN)

    override suspend fun login(token: String, username: String, password: String): TokenResponse =
        client.post(Url.LOGIN_USER) {
            contentType(ContentType.Application.Json)
            val map = mapOf(
                "request_token" to token,
                "username" to username,
                "password" to password
            )
            body = map
        }

    override suspend fun createSession(token: String): SessionResponse =
        client.post(Url.CREATE_SESSION) {
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