package com.example.movieappapi.domain.model.room


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieappapi.domain.model.AccountStatesResponse
import com.example.movieappapi.domain.model.Genre

@Entity(tableName = "movieDetailsEntity")
data class MovieDetailsEntity(
    @PrimaryKey
    var id: Int,
    var genres: List<Genre>,
    var overview: String,
    var popularity: Double,
    var posterPath: String,
    var status: String,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int,
    var accountStatesResponse: AccountStatesResponse?
)