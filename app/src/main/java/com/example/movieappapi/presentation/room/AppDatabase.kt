package com.example.movieappapi.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieappapi.domain.model.room.MovieDetailsEntity
import com.example.movieappapi.domain.model.room.MovieEntity
import com.example.movieappapi.domain.model.room.SessionEntity
import com.example.movieappapi.domain.model.room.UserEntity
import com.example.movieappapi.presentation.room.converters.AccountResponseConverter
import com.example.movieappapi.presentation.room.converters.GenresConverter
import com.example.movieappapi.presentation.room.converters.SessionConverter

@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class, SessionEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(AccountResponseConverter::class, GenresConverter::class, SessionConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): AppDao

}