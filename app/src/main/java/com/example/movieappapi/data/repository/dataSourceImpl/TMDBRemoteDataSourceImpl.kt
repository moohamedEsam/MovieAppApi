package com.example.movieappapi.data.repository.dataSourceImpl

import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

const val SESSION_PARAMETER = "session_id"

class TMDBRemoteDataSourceImpl(private val client: HttpClient) : TMDBRemoteDataSource {

    override suspend fun getPopularTv(): TvShowsResponse = client.get(Url.POPULAR_Tv)


    override suspend fun getAccountDetails(token: String): AccountDetailsResponse =
        client.get(Url.ACCOUNT_DETAILS) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun createGuestSession(): GuestSessionResponse = client.post(Url.GUEST_LOGIN)

    override suspend fun discoverMovies(page: Int): MoviesResponse =
        client.get(Url.DISCOVER_MOVIES) {
            parameter("page", page)
        }

    override suspend fun getUserCreatedList(accountId: Int, token: String): UserListsResponse =
        client.get(Url.getUserLists(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun createList(
        token: String,
        name: String,
        description: String
    ): CreateListResponse =
        client.post(Url.CREATE_LIST) {
            parameter(SESSION_PARAMETER, token)
            contentType(ContentType.Application.Json)
            body = mapOf(
                "name" to name,
                "description" to description
            )
        }

    override suspend fun deleteList(token: String, listId: Int): RateMediaResponse =
        client.delete(Url.deleteList(listId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun clearList(token: String, listId: Int): RateMediaResponse =
        client.post(Url.clearList(listId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun addMovieToList(
        token: String,
        listId: Int,
        movieId: Int
    ): RateMediaResponse = client.post(Url.addMovieToList(listId)) {
        parameter(SESSION_PARAMETER, token)
        contentType(ContentType.Application.Json)
        body = mapOf("media_id" to movieId)
    }

    override suspend fun removeMovieToList(
        token: String,
        listId: Int,
        movieId: Int
    ): RateMediaResponse = client.post(Url.removeMovieToList(listId)) {
        parameter(SESSION_PARAMETER, token)
        contentType(ContentType.Application.Json)
        body = mapOf("media_id" to movieId)
    }

    override suspend fun getList(listId: Int): UserListDetailsResponse =
        client.get(Url.getList(listId))

    override suspend fun getUserFavoriteMovies(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserFavoriteMovies(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun getMovieDetails(movieId: Int, sessionId: String): MovieDetailsResponse =
        client.get(Url.getMovieDetails(movieId)) {
            parameter(SESSION_PARAMETER, sessionId)
            parameter("append_to_response", "account_states")
        }

    override suspend fun getUserFavoriteTv(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserFavoriteTv(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun getUserMovieWatchList(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserWatchListMovies(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun getUserRatedMovies(accountId: Int, token: String): MoviesResponse =
        client.get(Url.getUserRatedMovies(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun markAsFavorite(
        accountId: Int,
        token: String,
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean
    ): RateMediaResponse = client.post(Url.markAsFavorite(accountId)) {
        parameter(SESSION_PARAMETER, token)
        header("Content-Type", "application/json;charset=utf-8")
        contentType(ContentType.Application.Json)
        body = MarkMediaFavoriteRequestBody(mediaType, mediaId, isFavorite)
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
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun getUserRatedTvEpisodes(
        accountId: Int,
        token: String
    ): UserRatedTvEpisodesResponse =
        client.get(Url.getUserRatedTvEpisodes(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun getUserTvWatchList(accountId: Int, token: String): TvShowsResponse =
        client.get(Url.getUserWatchListTv(accountId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun rateMovie(
        movieId: Int,
        token: String,
        value: Float
    ): RateMediaResponse = client.post(Url.rateMovie(movieId)) {
        parameter(SESSION_PARAMETER, token)
        contentType(ContentType.Application.Json)
        body = mapOf(
            "value" to value
        )
    }

    override suspend fun deleteMovieRating(movieId: Int, token: String): RateMediaResponse =
        client.delete(Url.deleteMovieRating(movieId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun deleteTvRating(tvId: Int, token: String): RateMediaResponse =
        client.delete(Url.deleteTvRating(tvId)) {
            parameter(SESSION_PARAMETER, token)
        }

    override suspend fun rateTv(tvId: Int, token: String, value: Float): RateMediaResponse =
        client.post(Url.rateTv(tvId)) {
            parameter(SESSION_PARAMETER, token)
            contentType(ContentType.Application.Json)
            body = mapOf(
                "value" to value
            )
        }

    override suspend fun discoverTv(): TvShowsResponse = client.get(Url.DISCOVER_MOVIES)

    override suspend fun requestToken(): TokenResponse =
        client.get(Url.REQUEST_TOKEN)

    override suspend fun login(token: String, username: String, password: String): TokenResponse =
        client.post(Url.LOGIN_USER) {
            contentType(ContentType.Application.Json)
            body = mapOf(
                "request_token" to token,
                "username" to username,
                "password" to password
            )
        }

    override suspend fun createSession(token: String): SessionResponse =
        client.post(Url.CREATE_SESSION) {
            url {
                parameter("request_token", token)
            }
        }

    override suspend fun getPopularMovies(page: Int): MoviesResponse =
        client.get(Url.POPULAR_MOVIES) {
            parameter("page", page)
        }

    override suspend fun getTopRatedMovies(page: Int): MoviesResponse =
        client.get(Url.TOP_RATED_MOVIES) {
            parameter("page", page)
        }

    override suspend fun getNowPlayingMovies(page: Int): MoviesResponse =
        client.get(Url.NOW_PLAYING_MOVIES) {
            parameter("page", page)
        }

    override suspend fun getUpcomingMovies(page: Int): MoviesResponse =
        client.get(Url.UPCOMING_MOVIES) {
            parameter("page", page)
        }

    override suspend fun getRecommendations(movieId: Int, page: Int): MoviesResponse =
        client.get(Url.getRecommendations(movieId)) {
            parameter("page", page)
        }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): MoviesResponse =
        client.get(Url.getSimilarMovies(movieId)) {
            parameter("page", page)
        }

    override suspend fun searchAll(query: String): AllSearchResponse = client.get(Url.SEARCH_ALL) {
        url {
            parameter("query", query)
        }
    }

    override suspend fun searchMovie(query: String, page: Int): MoviesResponse =
        client.get(Url.SEARCH_MOVIE) {
            parameter("query", query)
            parameter("page", page)
        }

    override suspend fun searchTv(query: String): TvShowsResponse = client.get(Url.SEARCH_TV) {
        url {
            parameter("query", query)
        }
    }

    override suspend fun getGenres(): GenreResponse = client.get(Url.GENRES)
}