package com.example.movieappapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.composables.MainScreen
import com.example.movieappapi.ui.theme.MovieAppApiTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalSerializationApi
    @ExperimentalCoilApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

