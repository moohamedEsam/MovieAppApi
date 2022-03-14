package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.model.TvShowsResponse

interface TVRemoteDataSource {

    suspend fun getPopularTv(): TvShowsResponse

    suspend fun searchTv(query: String): TvShowsResponse

    suspend fun discoverTv(): TvShowsResponse

    suspend fun rateTv(tvId: Int, token: String, value: Float): RateMediaResponse

    suspend fun deleteTvRating(tvId: Int, token: String): RateMediaResponse
}