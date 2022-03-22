package com.example.movieappapi.presentation.room.converters

import androidx.room.TypeConverter
import com.example.movieappapi.domain.model.Genre
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenresConverter {

    @TypeConverter
    fun fromGenres(genres: List<Genre>) = Json.encodeToString(genres)

    @TypeConverter
    fun toGenres(genres: String): List<Genre> = Json.decodeFromString(genres)
}