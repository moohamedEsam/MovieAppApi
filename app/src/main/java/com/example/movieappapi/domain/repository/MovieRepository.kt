package com.example.movieappapi.domain.repository

import com.example.movieappapi.domain.model.*
import com.example.movieappapi.domain.model.room.MovieEntity
import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.domain.model.room.UserListDetailsEntity
import com.example.movieappapi.domain.utils.MainFeedMovieListType
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.*


interface MovieRepository {

    suspend fun isCurrentUserGuest(): Boolean

    suspend fun requestToken(): Resource<Boolean>

    suspend fun login(userEntity: UserEntity): Resource<Boolean>

    suspend fun setAccountDetails()

    suspend fun getAccountStatus(): StateFlow<UserStatus>

    suspend fun resetRepository()

    suspend fun createSession(): Resource<Boolean>

    suspend fun createGuestSession(): Resource<Boolean>

    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailsResponse>

    suspend fun getMainFeedMovies(mainFeedMovieListType: MainFeedMovieListType): Resource<MoviesResponse>

    suspend fun discoverMovies(
        params: HashMap<String, String>,
        page: Int
    ): Resource<MoviesResponse>


    suspend fun getRecommendations(movieId: Int, page: Int = 0): Resource<MoviesResponse>

    suspend fun getSimilarMovies(movieId: Int, page: Int = 0): Resource<MoviesResponse>

    suspend fun getUserFavoriteMovies(): Resource<MoviesResponse>

    suspend fun getUserMovieWatchList(): Resource<MoviesResponse>

    suspend fun getUserRatedMovies(): Resource<MoviesResponse>

    suspend fun searchAll(query: String): Resource<AllSearchResponse>

    suspend fun searchMovie(query: String, page: Int = 0): Resource<MoviesResponse>

    suspend fun searchTv(query: String): Resource<TvShowsResponse>

    suspend fun getGenres(): Resource<GenreResponse>

    suspend fun getUserCreatedList(): Resource<UserListsResponse>

    suspend fun createList(name: String, description: String): Resource<CreateListResponse>

    suspend fun addMovieToList(listId: Int, movieId: Int): Resource<RateMediaResponse>

    suspend fun getList(listId: Int): Resource<UserListDetailsResponse>

    suspend fun removeMovieFromList(listId: Int, movieId: Int): Resource<RateMediaResponse>

    suspend fun clearList(listId: Int): Resource<RateMediaResponse>

    suspend fun deleteList(listId: Int): Resource<RateMediaResponse>

    suspend fun markAsFavorite(
        mediaId: Int,
        mediaType: String,
        isFavorite: Boolean = true
    ): Resource<RateMediaResponse>

    suspend fun addToWatchList(
        mediaId: Int,
        mediaType: String,
        watchList: Boolean
    ): Resource<RateMediaResponse>

    suspend fun rateMovie(
        movieId: Int,
        value: Float
    ): Resource<RateMediaResponse>

    suspend fun deleteRateMovie(movieId: Int): Resource<RateMediaResponse>

    suspend fun rateTv(
        tvId: Int,
        value: Float
    ): Resource<RateMediaResponse>

    suspend fun deleteRateTv(tvId: Int): Resource<RateMediaResponse>

    suspend fun getPopularTv(): Resource<TvShowsResponse>

    suspend fun getUserFavoriteTv(): Resource<TvShowsResponse>

    suspend fun getUserRatedTv(): Resource<TvShowsResponse>

    suspend fun getUserRatedTvEpisodes(): Resource<UserRatedTvEpisodesResponse>

    suspend fun getUserTvWatchList(): Resource<TvShowsResponse>

    suspend fun discoverTv(): Resource<TvShowsResponse>

    suspend fun assignCachedSession()

    suspend fun updateSession()

    suspend fun getSession(userEntity: UserEntity): Resource<Boolean>

    suspend fun getCachedUser(): UserEntity?

    suspend fun getLatestMovieAdded(tag: String): Date?

    suspend fun updateUser(userEntity: UserEntity)

    suspend fun getLocalSession(): SessionResponse

    suspend fun deleteAllMovies(tag: String)


    suspend fun updateMovie(movie: MovieEntity)

    suspend fun getMovie(movieId: Int): MovieEntity?

    suspend fun getLocalMovies(movieListType: MainFeedMovieListType): List<Movie>

    fun getLocalMovieDetails(movieId: Int): Flow<MovieDetailsResponse?>

    suspend fun insertLocalMovies(movies: List<Movie>, tag: String)

    suspend fun insertLocalMovieDetails(movie: MovieDetailsResponse)

    suspend fun updateLocalMovieDetails(movie: MovieDetailsResponse)

    suspend fun insertUserListDetails(userListDetailsResponse: UserListDetailsResponse): Resource<Unit>
    suspend fun updateUserListDetails(userListDetailsResponse: UserListDetailsResponse): Resource<Unit>
    suspend fun updateUserListDetails(userListDetailsEntity: UserListDetailsEntity): Resource<Unit>
    suspend fun deleteUserListDetails(listId: Int): Resource<Unit>
    fun getUserListDetails(listId: Int): Flow<Resource<UserListDetailsResponse>>
    fun getUserLists(): Flow<Resource<UserListsResponse>>
    fun getUserListDetailsEntity(listId: Int): Flow<Resource<UserListDetailsEntity>>
}