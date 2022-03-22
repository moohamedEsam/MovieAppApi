package com.example.movieappapi.domain.utils.mappers

import com.example.movieappapi.domain.model.SessionResponse
import com.example.movieappapi.domain.model.room.SessionEntity
import java.util.*

fun SessionResponse.toSessionEntity(expiresAt: Date) =
    SessionEntity(sessionId ?: "", success ?: false, expiresAt)

fun SessionEntity.toSessionResponse() = SessionResponse(sessionId, success)