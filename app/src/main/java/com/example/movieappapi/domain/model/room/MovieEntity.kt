package com.example.movieappapi.domain.model.room


import androidx.room.Entity
import java.util.*

@Entity(tableName = "movieEntity", primaryKeys = ["id", "tag"])
data class MovieEntity(
    var id: Int,
    var posterPath: String,
    var title: String,
    val dateAdded: Date? = null,
    var tag: String = "popular"
)