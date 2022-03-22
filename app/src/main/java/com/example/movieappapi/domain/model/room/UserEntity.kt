package com.example.movieappapi.domain.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userEntity")
data class UserEntity(
    val username: String,
    val password: String,
    val loggedIn: Boolean,
    @PrimaryKey
    val id: Int = 1
)