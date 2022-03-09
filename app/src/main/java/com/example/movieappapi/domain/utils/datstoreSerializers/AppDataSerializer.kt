package com.example.movieappapi.domain.utils.datstoreSerializers

import android.util.Log
import androidx.datastore.core.Serializer
import com.example.movieappapi.AppData
import java.io.InputStream
import java.io.OutputStream

object AppDataSerializer : Serializer<AppData> {
    override val defaultValue: AppData
        get() = AppData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppData {
        return try {
            AppData.parseFrom(input)
        } catch (exception: Exception) {
            Log.e("TokenSerializer", "readFrom: ${exception.message}")
            AppData.getDefaultInstance()
        }
    }

    override suspend fun writeTo(t: AppData, output: OutputStream) = t.writeTo(output)
}