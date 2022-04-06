package com.example.movieappapi.presentation.screen.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.presentation.components.GridMovieList
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import com.example.movieappapi.presentation.components.StateFullSearchComposable
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoilApi::class)
@Composable
fun SearchMovieScreen(
    navHostController: NavHostController
) {
    val viewModel: SearchViewModel = getViewModel()
    val filteredMovies by viewModel.filteredItems
    val movies by viewModel.results
    val searchMode by viewModel.searchMode

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SearchComposable(
                onValueChanged = { viewModel.setFilteredItems(it) }
            ) {
                viewModel.setMovies(it)
            }
            GridMovieList(
                movies = if (searchMode) filteredMovies?.results
                    ?: emptyList() else movies.data?.results ?: emptyList(),
                navHostController = navHostController
            ) {
                if (!searchMode)
                    viewModel.paginateMovies()
            }
        }

        ResourceErrorSnackBar(resource = movies) {
            viewModel.paginateMovies()
        }
    }

}

@Composable
private fun SearchComposable(
    onValueChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }
    StateFullSearchComposable(
        value = value,
        onValueChange = {
            value = it
            onValueChanged(it)
        },
        onSearch = {
            value = ""
            onSearch(it)
        },
        label = "search movies"
    )
}