package com.example.movieappapi.presentation.room

import androidx.room.*
import com.example.movieappapi.domain.model.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("select * from userEntity")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(sessionEntity: SessionEntity)

    @Update
    suspend fun updateSession(sessionEntity: SessionEntity)

    @Query("delete from sessionEntity")
    suspend fun deleteSession()

    @Query("select * from sessionEntity")
    suspend fun getSession(): SessionEntity?

    @Insert
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Query("select * from movieEntity where id = (:movieId)")
    suspend fun getMovie(movieId: Int): MovieEntity?

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

    @Query("select * from movieEntity where tag = (:tag)")
    suspend fun getMovie(tag: String): List<MovieEntity>

    @Query("select dateAdded from movieEntity where dateAdded is not null and tag = (:tag) order by dateAdded desc")
    suspend fun getLatestMovieDate(tag: String): Date?

    @Query("delete from movieEntity where tag like (:tag)")
    suspend fun deleteAllMovies(tag: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetailsEntity: MovieDetailsEntity)

    @Update
    suspend fun updateMovieDetails(movieDetailsEntity: MovieDetailsEntity)

    @Delete
    suspend fun deleteMovieDetails(movieDetailsEntity: MovieDetailsEntity)

    @Query("select * from movieDetailsEntity where id = (:movieId)")
    fun getMovieDetails(movieId: Int): Flow<MovieDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserListDetails(userListDetailsEntity: UserListDetailsEntity)

    @Update
    suspend fun updateUserListDetails(userListDetailsEntity: UserListDetailsEntity)

    @Query("delete from userListDetailsEntity where id = (:listId)")
    suspend fun deleteUserListDetails(listId: Int)

    @Query("select * from userListDetailsEntity where id = (:listId)")
    fun getUserListDetails(listId: Int): Flow<UserListDetailsEntity>

    @Query("select id, name, description,favoriteCount,itemCount, posterPath from userListDetailsEntity")
    fun getAllUserList(): Flow<List<UserListEntity>>
}