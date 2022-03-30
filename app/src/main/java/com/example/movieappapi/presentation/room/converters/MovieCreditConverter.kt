package com.example.movieappapi.presentation.room.converters

import androidx.room.TypeConverter
import com.example.movieappapi.domain.model.MovieCredits
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieCreditConverter {

    @TypeConverter
    fun toMovieCredit(movieCredit: String): MovieCredits =
        Json.decodeFromString(movieCredit)

    @TypeConverter
    fun fromMovieCredit(movieCredit: MovieCredits) =
        Json.encodeToString(movieCredit)
}