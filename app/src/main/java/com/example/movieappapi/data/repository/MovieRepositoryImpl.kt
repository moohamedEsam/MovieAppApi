package com.example.movieappapi.data.repository

import android.content.Context
import android.util.Log
import com.example.movieappapi.CachedUser
import com.example.movieappapi.Token
import com.example.movieappapi.data.repository.dataSource.MovieRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.datstoreSerializers.tokenDataStore
//import com.example.movieappapi.domain.utils.datstoreSerializers.tokenDataStore
import com.example.movieappapi.domain.utils.datstoreSerializers.userDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.text.DateFormat
import java.util.*

class MovieRepositoryImpl(
    private val remote: MovieRemoteDataSource
) : MovieRepository {

    private var tokenResponse = TokenResponse()
    private var sessionResponse = SessionResponse()
    private var genreResponse = GenreResponse()

    private suspend fun getTokenIfNull(context: Context) {
        if (tokenResponse.requestToken.isNullOrBlank())
            requestToken(context)
    }

    override suspend fun getToken(context: Context): Resource<Boolean> {
        assignCachedToken(context)
        if (tokenResponse.success != true)
            return requestToken(context)
        return Resource.Success(true)
    }

    override suspend fun requestToken(context: Context): Resource<Boolean> {
        return try {
            tokenResponse = remote.requestToken()
            //updateToken(context, tokenResponse.toToken())
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
        context.tokenDataStore.data.collectLatest {
            tokenResponse = it.toTokenResponse()
            tokenResponse.success = true
            val date = DateFormat.getInstance().parse(it.expiresAt)
            tokenResponse.success = date?.after(Date())
        }
    }

    override suspend fun updateToken(context: Context, token: Token) {
        context.tokenDataStore.updateData {
            it.toBuilder().setAccessToken(token.accessToken)
                .setExpiresAt(token.expiresAt)
                .build()
        }
    }

    override suspend fun getCachedUser(context: Context): Flow<CachedUser> =
        context.userDataStore.data

    override suspend fun updateUser(context: Context, user: CachedUser) {
        context.userDataStore.updateData {
            it.toBuilder()
                .setPassword(user.password)
                .setUsername(user.username)
                .setLoggedIn(user.loggedIn)
                .build()
        }
    }
}