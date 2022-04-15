package com.example.movieappapi.data.repository

import android.util.Log
import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.model.room.MovieEntity
import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.domain.model.room.UserListDetailsEntity
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.MainFeedMovieListType
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.UserStatus
import com.example.movieappapi.domain.utils.mappers.*
import com.example.movieappapi.presentation.room.AppDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class MovieRepositoryImpl(
    private val remote: TMDBRemoteDataSource,
    private val local: AppDao
) : MovieRepository {
    private var tokenResponse = TokenResponse()
    private var sessionResponse = SessionResponse()
    private var guestSessionResponse = GuestSessionResponse()
    private var genreResponse = GenreResponse()
    private var accountDetailsResponse: MutableStateFlow<UserStatus> =
        MutableStateFlow(UserStatus.LoggedOut)

    override suspend fun setAccountDetails() {
        try {
            val response = remote.getAccountDetails(getActiveToken())
            accountDetailsResponse.emit(UserStatus.LoggedIn(response))
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "setAccountDetails: ${exception.message}")
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
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocalMovies(movieListType: MainFeedMovieListType): List<Movie> =
        local.getMovie(movieListType.tag).map { it.toMovie() }

    override suspend fun deleteAllMovies(tag: String) = local.deleteAllMovies(tag)

    override fun getLocalMovieDetails(movieId: Int): Flow<MovieDetailsResponse?> =
        local.getMovieDetails(movieId).map { it?.toMovieDetailsResponse() }

    override suspend fun insertLocalMovies(movies: List<Movie>, tag: String) {
        movies.forEachIndexed { index, movie ->
            try {
                val movieEntity = if (index == 0) movie.toMovieEntity(Date(), tag)
                else
                    movie.toMovieEntity(tag = tag)
                local.insertMovie(movieEntity)

            } catch (exception: Exception) {
                Log.e(
                    "MovieRepositoryImpl",
                    "insertLocalMovies: ${exception.message} -> ${movie.title}"
                )
            }
        }
    }

    override suspend fun discoverMovies(
        params: HashMap<String, String>,
        page: Int
    ): Resource<MoviesResponse> {
        return try {
            val response = remote.discoverMovies(params, page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.i("MovieRepositoryImpl", "getKeywordMovies: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun getMovie(movieId: Int): MovieEntity? = local.getMovie(movieId)

    override suspend fun updateMovie(movie: MovieEntity) {
        local.updateMovie(movie)
    }

    override suspend fun insertLocalMovieDetails(movie: MovieDetailsResponse) {
        local.insertMovieDetails(movie.toMovieDetailsEntity())
    }

    override suspend fun isCurrentUserGuest(): Boolean =
        accountDetailsResponse.value.data?.id == null

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

            sessionResponse = remote.createSession(tokenResponse.requestToken ?: "")
            updateSession()
            Resource.Success(sessionResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "createSession: ${exception.message}")
            Resource.Error(exception.localizedMessage, sessionResponse.success ?: false)
        }
    }


    override suspend fun getMainFeedMovies(mainFeedMovieListType: MainFeedMovieListType): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getMainFeedMovies(mainFeedMovieListType)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getMainFeedMovies: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' })
        }
    }


    override suspend fun getRecommendations(movieId: Int, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getRecommendations(movieId, page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getRecommendations: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.getSimilarMovies(movieId, page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getSimilarMovies: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun searchAll(query: String): Resource<AllSearchResponse> {
        return try {
            val response: AllSearchResponse = remote.searchAll(query)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchAll: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun searchMovie(query: String, page: Int): Resource<MoviesResponse> {
        return try {
            val response: MoviesResponse = remote.searchMovie(query, page)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchMovie: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun searchTv(query: String): Resource<TvShowsResponse> {
        return try {
            val response: TvShowsResponse = remote.searchTv(query)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "searchTv: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun getGenres(): Resource<GenreResponse> {
        return try {
            if (genreResponse.genres == null)
                genreResponse = remote.getGenres()
            Resource.Success(genreResponse)
        } catch (exception: Exception) {
            Log.e("MovieRemoteDataSourceImpl", "getGenres: ${exception.localizedMessage}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun getLatestMovieAdded(tag: String): Date? =
        local.getLatestMovieDate(tag)

    override suspend fun assignCachedSession() {
        val local = local.getSession() ?: return
        local.success = local.expiresAt.after(Date())
        sessionResponse = local.toSessionResponse()
    }

    override suspend fun resetRepository() {
        accountDetailsResponse.emit(UserStatus.LoggedOut)
        sessionResponse = SessionResponse()
        tokenResponse = TokenResponse()
        guestSessionResponse = GuestSessionResponse()
    }


    override suspend fun createGuestSession(): Resource<Boolean> {
        return try {
            guestSessionResponse = remote.createGuestSession()
            accountDetailsResponse.emit(UserStatus.LoggedIn(AccountDetailsResponse()))
            Resource.Success(guestSessionResponse.success ?: false)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "createGuestSession: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserFavoriteMovies(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserFavoriteMovies(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
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
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun updateLocalMovieDetails(movie: MovieDetailsResponse) {
        try {
            local.updateMovieDetails(movie.toMovieDetailsEntity())
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "updateLocalMovieDetails: ${exception.message}")
        }
    }

    private fun getActiveToken() =
        sessionResponse.sessionId ?: guestSessionResponse.guestSessionId ?: ""

    override suspend fun getUserMovieWatchList(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserMovieWatchList(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserMovieWatchList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedMovies(): Resource<MoviesResponse> {
        return try {
            val response =
                remote.getUserRatedMovies(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserRatedMovies: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserCreatedList(): Resource<UserListsResponse> {
        return try {
            val response =
                remote.getUserCreatedList(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
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
            val response = remote.createList(getActiveToken(), name, description)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "createList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.addMovieToList(getActiveToken(), listId, movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "addMovieToList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun removeMovieFromList(
        listId: Int,
        movieId: Int
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.removeMovieToList(getActiveToken(), listId, movieId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "removeMovieToList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun clearList(listId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.clearList(getActiveToken(), listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "clearList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun deleteList(listId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteList(getActiveToken(), listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "deleteList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun getList(listId: Int): Resource<UserListDetailsResponse> {
        return try {
            val response = remote.getList(listId)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getList: ${exception.message}")
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' }
                ?: exception.message)
        }
    }

    override suspend fun markAsFavorite(
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.markAsFavorite(
                accountDetailsResponse.value.data?.id ?: 1,
                token = getActiveToken(),
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
        mediaType: String,
        watchList: Boolean
    ): Resource<RateMediaResponse> {
        return try {
            val response =
                remote.addToWatchList(
                    mediaId,
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken(),
                    mediaType,
                    watchList
                )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "addToWatchList: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getAccountStatus(): StateFlow<UserStatus> = accountDetailsResponse

    override suspend fun insertUserListDetails(userListDetailsResponse: UserListDetailsResponse): Resource<Unit> {
        return try {
            local.insertUserListDetails(userListDetailsResponse.toUserListDetailsEntity())
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "insertUserListDetails: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateUserListDetails(userListDetailsResponse: UserListDetailsResponse): Resource<Unit> {
        return try {
            local.updateUserListDetails(userListDetailsResponse.toUserListDetailsEntity())
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "insertUserListDetails: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteUserListDetails(listId: Int): Resource<Unit> {
        return try {
            local.deleteUserListDetails(listId)
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "insertUserListDetails: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    private fun mapMovieIdsToMovies(movieIds: List<Int>) = flow {
        val movies = mutableListOf<Movie>()
        movieIds.map {
            local.getMovie(it).let { movieEntity ->
                movieEntity?.let { movie ->
                    movies.add(movie.toMovie())
                }
            }
        }
        emit(movies.toList())
    }


    override fun getUserListDetails(listId: Int): Flow<Resource<UserListDetailsResponse>> =
        flow {
            try {
                local.getUserListDetails(listId).collectLatest { userList ->
                    mapMovieIdsToMovies(userList.items ?: emptyList()).collectLatest { movies ->
                        emit(Resource.Success(userList.toUserListDetailsResponse { movies }))
                    }
                }
            } catch (exception: Exception) {
                emit(Resource.Error(exception.message))
            }
        }

    override suspend fun updateUserListDetails(userListDetailsEntity: UserListDetailsEntity): Resource<Unit> {
        return try {
            local.updateUserListDetails(userListDetailsEntity)
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override fun getUserListDetailsEntity(listId: Int): Flow<Resource<UserListDetailsEntity>> =
        flow {
            try {
                local.getUserListDetails(listId).collectLatest {
                    emit(Resource.Success(it))
                }
            } catch (exception: Exception) {
                emit(Resource.Error(exception.message))
            }
        }

    override fun getUserLists(): Flow<Resource<UserListsResponse>> = channelFlow {
        try {
            local.getAllUserList().collectLatest {
                send(Resource.Success(it.toUserListsResponse()))
            }
        } catch (exception: Exception) {
            send(Resource.Error(exception.message))
        }
    }

    override suspend fun rateMovie(
        movieId: Int,
        value: Float
    ): Resource<RateMediaResponse> {
        return try {
            val response = remote.rateMovie(movieId, getActiveToken(), value)
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "rateMovie: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteRateMovie(movieId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteMovieRating(movieId, getActiveToken())
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "deleteRateMovie: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteRateTv(tvId: Int): Resource<RateMediaResponse> {
        return try {
            val response = remote.deleteTvRating(tvId, getActiveToken())
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
            val response = remote.rateTv(tvId, getActiveToken(), value)
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
                remote.getUserFavoriteTv(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserFavoriteTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedTv(): Resource<TvShowsResponse> {
        return try {
            val response =
                remote.getUserRatedTv(accountDetailsResponse.value.data?.id ?: 1, getActiveToken())
            Resource.Success(response)
        } catch (exception: Exception) {
            Log.e("MovieRepositoryImpl", "getUserRatedTv: ${exception.message}")
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserRatedTvEpisodes(): Resource<UserRatedTvEpisodesResponse> {
        return try {
            val response = remote.getUserRatedTvEpisodes(
                accountDetailsResponse.value.data?.id ?: 1,
                getActiveToken()
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
                remote.getUserTvWatchList(
                    accountDetailsResponse.value.data?.id ?: 1,
                    getActiveToken()
                )
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
            Resource.Error(exception.message?.substringAfter("[\"")?.takeWhile { it != '"' })
        }
    }

    override suspend fun updateSession() {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
            val date = format.parse(tokenResponse.expiresAt ?: "")
            local.deleteSession()
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