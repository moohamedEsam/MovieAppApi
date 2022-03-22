package com.example.movieappapi.presentation.room.converters

import androidx.room.TypeConverter
import java.util.*

class SessionConverter {

    @TypeConverter
    fun fromDate(date: Date) = date.time

    @TypeConverter
    fun toDate(date: Long) = Date(date)
}