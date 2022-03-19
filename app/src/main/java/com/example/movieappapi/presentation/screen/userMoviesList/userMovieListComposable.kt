package com.example.movieappapi.presentation.screen.userMoviesList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.utils.UserMovieList
import com.example.movieappapi.presentation.components.HorizontalListMovieItem
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchComposable()
        CreateVerticalSpacer()
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(
                if (searchMode)
                    searchResult
                else
                    movies.data?.results ?: emptyList()
            ) {
                HorizontalListMovieItem(it, navHostController)
            }
        }
    }
}

@Composable
fun SearchComposable() {
    var query by remember {
        mutableStateOf("")
    }
    val viewModel: UserMoviesListViewModel = getViewModel()
    TextField(
        value = query,
        onValueChange = {
            query = it
            if (it.isNotEmpty())
                viewModel.setSearchResults(it)
            else
                viewModel.setSearchMode(false)

        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "search movies") }
    )
}
