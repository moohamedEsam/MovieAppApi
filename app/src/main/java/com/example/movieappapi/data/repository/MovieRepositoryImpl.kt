package com.example.movieappapi.data.repository

import android.util.Log
import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.model.room.MovieEntity
import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieList
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.UserStatus
import com.example.movieappapi.domain.utils.mappers.*
import com.example.movieappapi.presentation.room.AppDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

class MovieRepositoryImpl(
    private val remote: TMDBRemoteDataSource,
    private val local: AppDao
) : MovieRepository {
    private var isUserGuest = false
    private var tokenResponse = TokenResponse()
    private var sessionResponse = SessionResponse()
    private var guestSessionResponse = GuestSessionResponse()
    private var genreResponse = GenreResponse()
    private var accountDetailsResponse = AccountDetailsResponse()

    override suspend fun getAccountDetails() {
        try {
            accountDetailsResponse = remote.getAccountDetails(getActiveToken() ?: "")
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getAccountDetails: ${exception.message}")
        }
    }

    override suspend fun getSession(
        userEntity: UserEntity
    ): Resource<Boolean> {
        return try {
            if (isSameCachedUser(userEntity))
                assignCachedSession()
            Resource.Success(sessionResponse.success == true)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getToken: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocalMovies(movieList: MainFeedMovieList): List<Movie> =
        local.getMovie(movieList.tag).map { it.toMovie() }

    override suspend fun deleteAllMovies(tag: String) = local.deleteAllMovies(tag)

    override suspend fun getLocalMovieDetails(movieId: Int): MovieDetailsResponse? =
        local.getMovieDetails(movieId)?.toMovieDetailsResponse()

    override suspend fun insertLocalMovies(movies: List<Movie>, tag: String) {
        Log.i("MovieRepositoryImpl", "insertLocalMovies: called with tag $tag")
        movies.forEachIndexed { index, movie ->
            try {
                val movieEntity = if (index == 0) movie.toMovieEntity(Date(), tag)
                else
                    movie.toMovieEntity(tag = tag)
                local.insertMovie(movieEntity)
                Log.i("MovieRepositoryImpl", "insertLocalMovies: done")
            } catch (exception: Exception) {
                Log.e(
                    "MovieRepositoryImpl",
                    "insertLocalMovies: ${exception.message} -> ${movie.title}"
                )
            }
        }
    }

    override suspend fun getMovie(movieId: Int): MovieEntity? = local.getMovie(movieId)

    override suspend fun updateMovie(movie: MovieEntity) {
        local.updateMovie(movie)
    }

    override suspend fun insertLocalMovieDetails(movie: MovieDetailsResponse) {
        local.insertMovieDetails(movie.toMovieDetailsEntity())
    }

    override suspend fun isCurrentUserGuest(): Boolean = isUserGuest

    override suspend fun getLocalSession(): SessionResponse = sessionResponse

    override suspend fun requestToken(): Resource<Boolean> {
        return try {
            tokenResponse = remote.requestToken()
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "requestToken: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }


    override suspend fun login(
        userEntity: UserEntity
    ): Resource<Boolean> {
        return try {
            tokenResponse = remote.login(
                tokenResponse.requestToken ?: "",
                userEntity.username,
                userEntity.password
            )
            Resource.Success(tokenResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "login: ${exception.message}")
            Resource.Error(exception.localizedMessage, tokenResponse.success ?: false)
        }
    }

    private suspend fun isSameCachedUser(userEntity: UserEntity) = userEntity == local.getUser()


    override suspend fun createSession(): Resource<Boolean> {
        return try {
            isUserGuest = false
            sessionResponse = remote.createSession(tokenResponse.requestToken ?: "")
            updateSession()
            Resource.Success(sessionResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "createSession: ${exception.message}")
            Resource.Error(exception.localizedMessage, sessionResponse.success ?: false)
        }
    }

    override suspend fun getPopularMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getPopularMovies(page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getPopularMovies: ${exception.localizedMessage}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getTopRatedMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getTopRatedMovies(page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getTopRatedMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getNowPlayingMovies(page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getNowPlayingMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getUpcomingMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getUpcomingMovies(page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getUpcomingMovies: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getRecommendations(movieId: Int, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getRecommendations(movieId, page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getRecommendations: ${exception.localizedMessage}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getSimilarMovies(movieId, page)
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

    override suspend fun searchMovie(query: String, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.searchMovie(query, page)
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

    override suspend fun getLatestMovieAdded(tag: String): MovieEntity? =
        local.getLatestMovieAdded(tag)

    override suspend fun assignCachedSession() {
        val session = local.getSession()
        session?.success = session?.expiresAt?.after(Date()) ?: false
        sessionResponse = session?.toSessionResponse() ?: return
    }

    override suspend fun resetRepository() {
        sessionResponse = SessionResponse()
        tokenResponse = TokenResponse()
        accountDetailsResponse = AccountDetailsResponse()
        isUserGuest = false
        guestSessionResponse = GuestSessionResponse()
    }


    override suspend fun createGuestSession(): Resource<Boolean> {
        return try {
            guestSessionResponse = remote.createGuestSession()
            isUserGuest = true
            Resource.Success(guestSessionResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "createGuestSession: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun discoverMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response = remote.discoverMovies(page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "discoverMovies: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserFavoriteMovies(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserFavoriteMovies(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserFavoriteMovies: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailsResponse> {
        return try {
            val response = remote.getMovieDetails(movieId, sessionResponse.sessionId ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getMovieDetails: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    private fun getActiveToken() =
        if (isUserGuest)
            guestSessionResponse.guestSessionId
        else
            sessionResponse.sessionId

    override suspend fun getUserMovieWatchList(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserMovieWatchList(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserMovieWatchList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedMovies(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserRatedMovies(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserRatedMovies: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserCreatedList(): Resource<UserListsResponse> {
        return try {
            val response =
                remote.getUserCreatedList(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserCreatedList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun createList(
        name: String,
        description: String
    ): Resource<CreateListResponse> {
        return try {
            val response = remote.createList(getActiveToken() ?: "", name, description)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "createList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.addMovieToList(getActiveToken() ?: "", listId, movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "addMovieToList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun removeMovieToList(listId: Int, movieId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.removeMovieToList(getActiveToken() ?: "", listId, movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "removeMovieToList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun clearList(listId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.clearList(getActiveToken() ?: "", listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "clearList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun deleteList(listId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteList(getActiveToken() ?: "", listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "deleteList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun getList(listId: Int): Resource<UserListDetailsResponse> {
        return try {
            val response = remote.getList(listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getList: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    override suspend fun markAsFavorite(
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.markAsFavorite(
                accountDetailsResponse.id ?: 1,
                token = getActiveToken() ?: "",
                mediaId = mediaId,
                mediaType = mediaType,
                isFavorite = isFavorite
            )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "markAsFavorite: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun addToWatchList(
        mediaId: Int,
        mediaType: String
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.addToWatchList(mediaId, accountDetailsResponse.id ?: 1, mediaType)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "addToWatchList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getAccountStatus(): UserStatus {
        return if (isUserGuest && accountDetailsResponse.id == null)
            UserStatus.Guest
        else if (accountDetailsResponse.id != null)
            UserStatus.LoggedIn(accountDetailsResponse)
        else
            UserStatus.LoggedOut
    }

    override suspend fun rateMovie(
        movieId: Int,
        value: Float
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.rateMovie(movieId, getActiveToken() ?: "", value)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "rateMovie: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteRateMovie(movieId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteMovieRating(movieId, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "deleteRateMovie: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteRateTv(tvId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteTvRating(tvId, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "deleteRateTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun rateTv(
        tvId: Int,
        value: Float
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.rateTv(tvId, getActiveToken() ?: "", value)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "rateTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getPopularTv(): Resource<TvShowsResponse> {
        return try {
            val response = remote.getPopularTv()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getPopularTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserFavoriteTv(): Resource<TvShowsResponse> {
        return try {
            val response =
                remote.getUserFavoriteTv(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserFavoriteTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedTv(): Resource<TvShowsResponse> {
        return try {
            val response =
                remote.getUserRatedTv(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserRatedTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedTvEpisodes(): Resource<UserRatedTvEpisodesResponse> {
        return try {
            val response = remote.getUserRatedTvEpisodes(
                accountDetailsResponse.id ?: 1,
                getActiveToken() ?: ""
            )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserRatedTvEpisodes: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTvWatchList(): Resource<TvShowsResponse> {
        return try {
            val response =
                remote.getUserTvWatchList(accountDetailsResponse.id ?: 1, getActiveToken() ?: "")
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserTvWatchList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun discoverTv(): Resource<TvShowsResponse> {
        return try {
            val response = remote.discoverTv()
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "discoverTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateSession() {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
            val date = format.parse(tokenResponse.expiresAt ?: "")
            local.insertSession(sessionResponse.toSessionEntity(date ?: Date()))
        } catch (e: Exception) {
            Log.e("MovieRepositoryImpl", "updateSession: ${e.message}")
        }
    }

    override suspend fun getCachedUser(): UserEntity? = local.getUser()


    override suspend fun updateUser(
        userEntity: UserEntity
    ) {
        local.insertUser(userEntity)
    }
}