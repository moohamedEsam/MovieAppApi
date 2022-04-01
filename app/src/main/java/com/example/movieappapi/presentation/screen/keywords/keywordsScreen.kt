package com.example.movieappapi.presentation.screen.keywords

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.model.Keyword
import com.example.movieappapi.presentation.components.MovieListComposable
import org.koin.androidx.compose.getViewModel

@Composable
fun KeywordMovieScreen(keyword: Keyword, navHostController: NavHostController) {
    val viewModel: KeywordsViewModel = getViewModel()
    val filteredMovies by viewModel.filteredMovies
    LaunchedEffect(key1 = Unit) {
        viewModel.setKeywordId(keyword.id ?: 0)
        viewModel.setMovies()
    }
    filteredMovies.HandleResourceChange {
        MovieListComposable(
            movies = it.results ?: emptyList(),
            label = "search keyword movies",
            navHostController = navHostController,
            onSearchValueChange = { query ->
                viewModel.setFilteredMovies(query)
            },
            onSearch = {}
        )
    }
}