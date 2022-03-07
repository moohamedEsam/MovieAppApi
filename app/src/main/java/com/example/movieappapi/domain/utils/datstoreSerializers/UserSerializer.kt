package com.example.movieappapi.domain.utils.datstoreSerializers

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.movieappapi.CachedUser
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<CachedUser> {
    override val defaultValue: CachedUser
        get() = CachedUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CachedUser {
        return try {
            CachedUser.parseFrom(input)
        } catch (exception: Exception) {
            Log.e("UserSerializer", "readFrom: ${exception.message}")
            CachedUser.getDefaultInstance()
        }
    }

    override suspend fun writeTo(t: CachedUser, output: OutputStream) = t.writeTo(output)
}

val Context.userDataStore: DataStore<CachedUser> by dataStore(
    "settings.pb",
    UserSerializer
)