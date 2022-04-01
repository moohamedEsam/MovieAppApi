package com.example.movieappapi.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun MovieListComposable(
    movies: List<Movie>?,
    label: String,
    navHostController: NavHostController,
    onSearchValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchComposable(
            label = label,
            onValueChange = { onSearchValueChange(it) }
        ) {
            onSearch(it)
        }
        CreateVerticalSpacer()
        GridMovieList(movies = movies ?: emptyList(), navHostController = navHostController)
    }
}