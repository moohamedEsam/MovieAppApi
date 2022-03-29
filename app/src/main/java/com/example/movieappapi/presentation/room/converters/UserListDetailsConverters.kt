package com.example.movieappapi.presentation.room.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserListDetailsConverters {

    @TypeConverter
    fun toAccountResponse(movieIds: String): List<Int> =
        Json.decodeFromString(movieIds)

    @TypeConverter
    fun fromAccountResponse(movieIds: List<Int>) =
        Json.encodeToString(movieIds)
}