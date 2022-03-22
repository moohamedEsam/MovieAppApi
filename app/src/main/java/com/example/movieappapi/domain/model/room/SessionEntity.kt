package com.example.movieappapi.domain.model.room


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sessionEntity")
data class SessionEntity(
    @PrimaryKey
    var sessionId: String,
    var success: Boolean,
    var expiresAt: Date
)