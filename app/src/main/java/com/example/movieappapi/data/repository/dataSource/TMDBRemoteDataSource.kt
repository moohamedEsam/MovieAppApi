package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.*

interface TMDBRemoteDataSource : MovieRemoteDataSource, TVRemoteDataSource {

    suspend fun requestToken(): TokenResponse

    suspend fun login(token: String, username: String, password: String): TokenResponse

    suspend fun getAccountDetails(token: String): AccountDetailsResponse

    suspend fun createSession(token: String): SessionResponse

    suspend fun createGuestSession(): GuestSessionResponse

    suspend fun getUserCreatedList(accountId: Int, token: String): UserListsResponse

    suspend fun getUserFavoriteMovies(accountId: Int, token: String): MoviesResponse

    suspend fun getUserMovieWatchList(accountId: Int, token: String): MoviesResponse

    suspend fun getUserRatedMovies(accountId: Int, token: String): MoviesResponse

    suspend fun getUserFavoriteTv(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserTvWatchList(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserRatedTv(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserRatedTvEpisodes(accountId: Int, token: String): UserRatedTvEpisodesResponse

    suspend fun createList(token: String, name: String, description: String): CreateListResponse

    suspend fun deleteList(token: String, listId: Int): RateMediaResponse

    suspend fun getList(listId: Int): UserListDetailsResponse

    suspend fun clearList(token: String, listId: Int): RateMediaResponse

    suspend fun addMovieToList(token: String, listId: Int, movieId: Int): RateMediaResponse

    suspend fun removeMovieToList(token: String, listId: Int, movieId: Int): RateMediaResponse

    suspend fun markAsFavorite(
        accountId: Int,
        token: String,
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean = true
    ): RateMediaResponse

    suspend fun addToWatchList(mediaId: Int, accountId: Int, mediaType: String): RateMediaResponse

    suspend fun searchAll(query: String): AllSearchResponse

    suspend fun getGenres(): GenreResponse

    suspend fun getKeywordMovies(keywordId: Int): MoviesResponse
}
