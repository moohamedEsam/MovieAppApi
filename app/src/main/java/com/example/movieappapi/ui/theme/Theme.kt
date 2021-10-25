package com.example.movieappapi.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.Green,
    primaryVariant = Color.Green,
    secondary = Color.DarkGray,
    onPrimary = Color.DarkGray,
    onBackground = Color.White,
    onSecondary = Color.LightGray,
    background = Color.Black
)


@Composable
fun MovieAppApiTheme(
    content: @Composable() () -> Unit
) {
    val colors =
        DarkColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}