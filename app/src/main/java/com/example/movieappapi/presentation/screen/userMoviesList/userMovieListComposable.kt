package com.example.movieappapi.presentation.screen.userMoviesList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.utils.UserMovieList
import com.example.movieappapi.presentation.components.HorizontalListMovieItem
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun UserMovieListScreen(
    navHostController: NavHostController,
    listType: UserMovieList
) {
    val viewModel: UserMoviesListViewModel = getViewModel()
    val movies by viewModel.movies
    LaunchedEffect(key1 = Unit) {
        viewModel.setMovies(listType)
    }
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(movies.data?.results ?: emptyList()) {
            HorizontalListMovieItem(it, navHostController)
        }
    }
}