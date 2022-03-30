package com.example.movieappapi.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import com.example.movieappapi.domain.utils.Url

suspend fun getPalette(
    url: String,
    context: Context,
    onPaletteGenerated: (Palette) -> Unit
): Bitmap? {
    val drawable = Coil.execute(
        ImageRequest
            .Builder(context).data(Url.getImageUrl(url))
            .allowConversionToBitmap(true)
            .crossfade(500)
            .crossfade(true)
            .allowHardware(false)
            .build()
    ).drawable
    if (drawable is BitmapDrawable) {
        val bitmap = drawable.bitmap

        val palette = Palette.from(bitmap).generate()
        onPaletteGenerated(palette)
        return bitmap
    }
    return null
}