package com.example.movieappapi.presentation.room.converters

import androidx.room.TypeConverter
import com.example.movieappapi.domain.model.AccountStatesResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AccountResponseConverter {

    @TypeConverter
    fun toAccountResponse(accountStatesResponse: String): AccountStatesResponse =
        Json.decodeFromString(accountStatesResponse)

    @TypeConverter
    fun fromAccountResponse(accountStatesResponse: AccountStatesResponse) =
        Json.encodeToString(accountStatesResponse)
}