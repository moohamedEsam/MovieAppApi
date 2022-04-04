package com.example.movieappapi.presentation.screen.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.model.Keyword
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.presentation.components.DiscoverFilterDialog
import com.example.movieappapi.presentation.components.MovieListComposable
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun DiscoverMovieScreen(
    params: HashMap<String, String>,
    navHostController: NavHostController
) {
    val viewModel: DiscoverViewModel = getViewModel()
    val filteredMovies by viewModel.filteredItems
    val movies by viewModel.results
    val searchMode by viewModel.searchMode
    var showDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Keyword) {
        viewModel.setParams(params)
        viewModel.setMovies()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            MovieListComposable(
                movies = if (searchMode)
                    filteredMovies?.results ?: emptyList()
                else
                    movies.data?.results ?: emptyList(),
                label = "search movies",
                //leadingIcon = Icons.Default.FilterList,
                navHostController = navHostController,
                onLeadingIconClicked = {
                    showDialog = true
                },
                onSearchValueChange = { query ->
                    viewModel.setFilteredItems(query)
                },
                onSearch = {
                    viewModel.searchByQuery(it)
                }
            ) {
                if (!searchMode && movies is Resource.Success)
                    viewModel.setMovies()
            }
            if (movies is Resource.Success)
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
        }

        ResourceErrorSnackBar(resource = movies) {
            viewModel.setMovies()
        }
        if (showDialog)
            DiscoverFilterDialog {
                showDialog = false
            }
    }

}