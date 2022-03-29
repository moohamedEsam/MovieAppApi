package com.example.movieappapi.presentation.screen.lists

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.movieappapi.presentation.components.MovieListComposable
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun ListScreen(listId: Int, navHostController: NavHostController) {
    val viewModel: ListViewModel = getViewModel()
    val searchMode by viewModel.searchMode
    val searchResult by viewModel.searchResult
    val movies by viewModel.list
    LaunchedEffect(key1 = Unit) {
        viewModel.setListId(listId)
        viewModel.getList()
    }

    MovieListComposable(
        movies = if (searchMode) searchResult else movies.data?.items,
        navHostController = navHostController,
        onSearchValueChange = {
            if (it.isNotEmpty())
                viewModel.setSearchResults(it)
            else
                viewModel.setSearchMode(false)

        }
    ) {

    }

}