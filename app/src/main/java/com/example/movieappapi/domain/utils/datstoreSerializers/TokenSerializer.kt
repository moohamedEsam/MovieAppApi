package com.example.movieappapi.domain.utils.datstoreSerializers

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.movieappapi.Token
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<Token> {
    override val defaultValue: Token
        get() = Token.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Token {
        return try {
            Token.parseFrom(input)
        } catch (exception: Exception) {
            Log.e("TokenSerializer", "readFrom: ${exception.message}")
            Token.getDefaultInstance()
        }
    }

    override suspend fun writeTo(t: Token, output: OutputStream) = t.writeTo(output)
}

val Context.tokenDataStore: DataStore<Token> by dataStore(
    "settings.pb",
    TokenSerializer
)