package com.example.movieappapi.domain.model.room


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movieEntity")
data class MovieEntity(
    @PrimaryKey
    var id: Int,
    var posterPath: String,
    var title: String,
    val dateAdded: Date? = null
)