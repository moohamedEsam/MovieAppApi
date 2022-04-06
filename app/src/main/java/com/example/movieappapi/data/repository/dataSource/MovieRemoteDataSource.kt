package com.example.movieappapi.data.repository.dataSource

import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.model.RateMediaResponse
import com.example.movieappapi.domain.utils.MainFeedMovieListType

interface MovieRemoteDataSource {

    suspend fun getMovieDetails(movieId: Int, sessionId: String): MovieDetailsResponse

    suspend fun getMainFeedMovies(mainFeedMovieListType: MainFeedMovieListType): MoviesResponse


    suspend fun getRecommendations(movieId: Int, page: Int = 0): MoviesResponse

    suspend fun getSimilarMovies(movieId: Int, page: Int = 0): MoviesResponse

    suspend fun searchMovie(query: String, page: Int = 0): MoviesResponse

    suspend fun rateMovie(movieId: Int, token: String, value: Float): RateMediaResponse

    suspend fun deleteMovieRating(movieId: Int, token: String): RateMediaResponse

}