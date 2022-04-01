package com.example.movieappapi.presentation.screen.userMoviesList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.utils.UserMovieList
import com.example.movieappapi.presentation.components.MovieListComposable
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun UserMovieListScreen(
    navHostController: NavHostController,
    listType: UserMovieList
) {
    val viewModel: UserMoviesListViewModel = getViewModel()
    val movies by viewModel.movies
    val searchResult by viewModel.searchResult
    val searchMode by viewModel.searchMode
    LaunchedEffect(key1 = Unit) {
        viewModel.setMovies(listType)
    }
    MovieListComposable(
        movies = if (searchMode) searchResult else movies.data?.results,
        "search movies",
        navHostController = navHostController,
        onSearchValueChange = {
            if (it.isNotEmpty())
                viewModel.setSearchResults(it)
            else
                viewModel.setSearchMode(false)
        }
    ) {}
}


