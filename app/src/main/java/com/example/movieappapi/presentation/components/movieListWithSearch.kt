package com.example.movieappapi.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
    navHostController: NavHostController,
    onSearchValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchComposable {
            onSearchValueChange(it)
        }
        CreateVerticalSpacer()
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(movies ?: emptyList()) {
                HorizontalListMovieItem(it, navHostController)
            }
        }
    }
}