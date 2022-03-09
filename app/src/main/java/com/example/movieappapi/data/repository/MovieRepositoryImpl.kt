package com.example.movieappapi.data.repository

import android.content.Context
import android.util.Log
import com.example.movieappapi.AppData
import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.datastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import java.text.SimpleDateFormat
import java.util.*

class MovieRepositoryImpl(
    private val remote: TMDBRemoteDataSource
) : MovieRepository {

    private var tokenResponse = TokenResponse()
    private var sessionResponse = SessionResponse()
    private var genreResponse = GenreResponse()

    private suspend fun getTokenIfNull(context: Context) {
        if (tokenResponse.requestToken.isNullOrBlank())
            requestToken(context)
    }

    override suspend fun getToken(context: Context): Resource<Boolean> {
        return try {
            assignCachedToken(context)
            if (tokenResponse.success != true)
                return requestToken(context)
            Resource.Success(true)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getToken: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }

    }

    override suspend fun requestToken(context: Context): Resource<Boolean> {
        return try {
            tokenResponse = remote.requestToken()
            updateToken(context, tokenResponse.requestToken ?: "", tokenResponse.expiresAt ?: "")
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "requestToken: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }


    override suspend fun login(
        context: Context,
        username: String,
        password: String
    ): Resource<Boolean> {
        return try {
            getTokenIfNull(context)
            tokenResponse = remote.login(tokenResponse.requestToken ?: "", username, password)
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "login: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }

    override suspend fun createSession(): Resource<Boolean> {
        return try {
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

    override suspend fun assignCachedToken(context: Context) {
        val data = context.datastore.data.take(1).toList()[0]
        assignTokenFromDatastore(data)
    }

    private fun assignTokenFromDatastore(it: AppData) {
        try {
            tokenResponse.requestToken = it.accessToken
            tokenResponse.expiresAt = it.expiresAt
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = format.parse(it.expiresAt.substring(0, it.expiresAt.lastIndex - 2))
            tokenResponse.success = date?.after(Date())
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "assignTokenFromDatastore: ${exception.message}")
        }
    }

    override suspend fun updateToken(context: Context, accessToken: String, expiresAt: String) {
        context.datastore.updateData {
            it.toBuilder()
                .setAccessToken(tokenResponse.requestToken)
                .setExpiresAt(tokenResponse.expiresAt)
                .build()
        }
    }

    override suspend fun getCachedUser(context: Context): Flow<AppData> = context.datastore.data

    override suspend fun updateUser(
        context: Context,
        username: String,
        password: String,
        loggedIn: Boolean
    ) {
        context.datastore.updateData {
            it.toBuilder()
                .setUsername(username)
                .setPassword(password)
                .setLoggedIn(loggedIn)
                .build()
        }
    }
}