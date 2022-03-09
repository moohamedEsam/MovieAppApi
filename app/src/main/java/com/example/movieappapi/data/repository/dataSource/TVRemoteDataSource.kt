package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.TvShowsResponse

interface TVRemoteDataSource {

    suspend fun getPopularTv(): TvShowsResponse

    suspend fun getUserFavoriteTv(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserRatedTv(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserRatedTvEpisodes(accountId: Int, token: String): TvShowsResponse

    suspend fun getUserTvWatchList(accountId: Int, token: String): TvShowsResponse

    suspend fun searchTv(query: String): TvShowsResponse

    suspend fun discoverTv(): TvShowsResponse
}