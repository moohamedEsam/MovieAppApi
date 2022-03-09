package com.example.movieappapi.domain.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.movieappapi.AppData
import com.example.movieappapi.domain.utils.datstoreSerializers.AppDataSerializer

object Constants {
    const val API_KEY = "40e14090346c1cef685fee58b35496e6"
}


val Context.datastore: DataStore<AppData> by dataStore(
    "settings.pb",
    AppDataSerializer
)
