package com.example.movieappapi.data.repository

import android.util.Log
import com.example.movieappapi.data.repository.dataSource.MovieRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource

class MovieRepositoryImpl(
    private val remote: MovieRemoteDataSource
) : MovieRepository {

    private var tokenResponse = TokenResponse()
    private var sessionResponse = SessionResponse()
    private var genreResponse = GenreResponse()
    private suspend fun getTokenIfNull() {
        if (tokenResponse.requestToken.isNullOrBlank())
            requestToken()
    }


    override suspend fun requestToken(): Resource<Boolean> {
        return try {
            tokenResponse = remote.requestToken()
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "requestToken: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }


    override suspend fun login(username: String, password: String): Resource<Boolean> {
        return try {
            getTokenIfNull()
            tokenResponse = remote.login(tokenResponse.requestToken ?: "", username, password)
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "login: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }

    override suspend fun createSession(): Resource<Boolean> {
        return try {
            getTokenIfNull()
            sessionResponse = remote.createSession(tokenResponse.requestToken ?: "")
            Resource.Success(sessionResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "createSession: ${exception.message}")
            Resource.Error(exception.localizedMessage, sessionResponse.success ?: false)
        }
    }

    override suspend fun getPopularMovies(): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getPopularMovies()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getPopularMovies: ${exception.localizedMessage}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getTopRatedMovies(): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getTopRatedMovies()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getTopRatedMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getNowPlayingMovies(): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getNowPlayingMovies()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getNowPlayingMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getUpcomingMovies(): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getUpcomingMovies()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getUpcomingMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getRecommendations(movieId: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getRecommendations(movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getRecommendations: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getSimilarMovies(movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getSimilarMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun searchAll(query: String): Resource<AllSearchResponse> {
        return try {
            val response: AllSearchResponse = remote.searchAll(query)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchAll: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun searchMovie(query: String): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.searchMovie(query)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchMovie: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun searchTv(query: String): Resource<TvShowsResponse> {
        return try {
            val response: TvShowsResponse = remote.searchTv(query)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchTv: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getGenres(): Resource<GenreResponse> {
        return try {
            if (genreResponse.genres == null)
                genreResponse = remote.getGenres()
            Resource.Success(genreResponse)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getGenres: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

}