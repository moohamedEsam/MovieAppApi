package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.*

interface TMDBRemoteDataSource : MovieRemoteDataSource, TVRemoteDataSource {

    suspend fun requestToken(): TokenResponse

    suspend fun login(token: String, username: String, password: String): TokenResponse

    suspend fun getAccountDetails(token: String): AccountDetailsResponse

    suspend fun createSession(token: String): SessionResponse

    suspend fun createGuestSession(): GuestSessionResponse

    suspend fun getUserCreatedList(accountId: Int, token: String): UserListsResponse

    suspend fun markAsFavorite(mediaId: Int, accountId: Int, mediaType: String): RateMediaResponse

    suspend fun addToWatchList(mediaId: Int, accountId: Int, mediaType: String): RateMediaResponse

    suspend fun rateMovie(movieId: Int, token: String): RateMediaResponse

    suspend fun rateTv(tvId: Int, token: String): RateMediaResponse

    suspend fun searchAll(query: String): AllSearchResponse

    suspend fun getGenres(): GenreResponse
}
