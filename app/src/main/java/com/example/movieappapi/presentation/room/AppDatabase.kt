package com.example.movieappapi.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieappapi.domain.model.room.*
import com.example.movieappapi.presentation.room.converters.*

@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class,
        SessionEntity::class, UserEntity::class, UserListDetailsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    AccountResponseConverter::class,
    GenresConverter::class,
    SessionConverter::class,
    UserListDetailsConverters::class,
    MovieCreditConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): AppDao

}