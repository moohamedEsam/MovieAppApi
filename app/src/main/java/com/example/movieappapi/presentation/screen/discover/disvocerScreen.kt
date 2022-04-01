package com.example.movieappapi.presentation.screen.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.model.Keyword
import com.example.movieappapi.domain.utils.DiscoverType
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.presentation.components.MovieListComposable
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun DiscoverMovieScreen(
    name: String,
    id: Int,
    discoverType: DiscoverType,
    navHostController: NavHostController
) {
    val viewModel: DiscoverViewModel = getViewModel()
    val filteredMovies by viewModel.filteredItems
    val movies by viewModel.results
    val searchMode by viewModel.searchMode
    LaunchedEffect(key1 = Keyword) {
        viewModel.setKeywordId(id)
        viewModel.setMovies(discoverType)
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
                label = "search $name movies",
                navHostController = navHostController,
                onSearchValueChange = { query ->
                    viewModel.setFilteredItems(query)
                },
                onSearch = {}
            ) {
                if (!searchMode && movies is Resource.Success)
                    viewModel.setMovies(discoverType)
            }
            if (movies is Resource.Success)
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
        }

        ResourceErrorSnackBar(resource = movies) {
            viewModel.setMovies(discoverType)
        }
    }

}