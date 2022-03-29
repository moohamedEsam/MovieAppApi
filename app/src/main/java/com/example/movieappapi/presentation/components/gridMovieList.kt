package com.example.movieappapi.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.model.Movie

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun GridMovieList(
    movies: List<Movie>,
    navHostController: NavHostController,
    paginate: () -> Unit = {}
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(80.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            if (index == movies.lastIndex)
                paginate()
            HorizontalListMovieItem(movie = movie, navHostController = navHostController)
        }
    }
}