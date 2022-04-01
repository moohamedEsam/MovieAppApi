package com.example.movieappapi.presentation.screen.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.model.AllSearchResponse
import com.example.movieappapi.domain.model.SearchResult
import com.example.movieappapi.domain.utils.mappers.toMovie
import com.example.movieappapi.presentation.components.GridMovieList
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import com.example.movieappapi.presentation.components.SearchComposable
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import com.example.movieappapi.ui.theme.MovieAppApiTheme
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SearchScreen(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchTextField()
        //FilterBox()
        CreateVerticalSpacer()

        ResultList(navHostController)
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun ResultList(navHostController: NavHostController) {
    val viewModel: SearchViewModel = getViewModel()
    val results by viewModel.searchResults
    val searchMode by viewModel.searchMode
    val filteredItems by viewModel.filteredItems
    Box(modifier = Modifier.fillMaxSize()) {
        results.OnSuccessComposable {
            ShowItems(
                searchMode = searchMode,
                searchItems = it,
                filteredItems = filteredItems,
                navHostController = navHostController
            )
        }
        ResourceErrorSnackBar(resource = results) {
            viewModel.setSearchResults()
        }
    }

}

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
private fun ShowItems(
    searchMode: Boolean,
    searchItems: AllSearchResponse,
    filteredItems: List<SearchResult>,
    navHostController: NavHostController
) {
    if (!searchMode)
        GridMovieList(
            searchItems.searchResults?.map { it.toMovie() } ?: emptyList(),
            navHostController
        )
    else
        GridMovieList(filteredItems.map { it.toMovie() }, navHostController)
}

@Composable
fun FilterBox() {
    var showFilters by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        ExpandBox {
            showFilters = !showFilters
        }
        if (showFilters)
            FilterBoxContents()

    }
}

@Composable
private fun ExpandBox(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "filters")
        Spacer(modifier = Modifier.width(4.dp))
        Divider(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                onClick()
            }
        ) {
            Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
        }
    }
}

@Composable
fun FilterBoxContents() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TypeFilterRow()
        CreateVerticalSpacer(4.dp)
        GenreFilterRow()
        CreateVerticalSpacer(4.dp)
        SortFilterRow()
    }
}

@Composable
fun SortFilterRow() {
    val viewModel: SearchViewModel = getViewModel()
    val value by viewModel.sortByFilter
    FilterRow(
        title = "sort by", value = value, values = listOf(
            "vote count",
            "vote average",
            "popularity",
            "default"
        )
    ) {
        viewModel.setSortByFilter(it)
        viewModel.filterResults()
    }
}

@Composable
fun GenreFilterRow() {
    val viewModel: SearchViewModel = getViewModel()
    val genre by viewModel.genres
    val value by viewModel.genreFilter
    viewModel.setGenres()
    genre?.genres?.let {
        FilterRow(
            title = "genres",
            value = value,
            values = it.map { item -> item.name ?: "" }.plus("all")
        ) {
            viewModel.setGenreFilter(it)
            viewModel.filterResults()
        }
    }
}

@Composable
fun TypeFilterRow() {
    val viewModel: SearchViewModel = getViewModel()
    val type by viewModel.typeFilter
    FilterRow(
        title = "type",
        value = type,
        values = listOf(
            "movie",
            "tv",
            "person",
            "all"
        )
    ) {
        viewModel.setTypeFilter(it)
        viewModel.filterResults()
    }
}

@Composable
private fun FilterRow(
    title: String,
    value: String,
    values: List<String>,
    onChange: (String) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Text(text = value, color = MaterialTheme.colors.primary)
    }
    if (showDialog)
        FilterAlertDialog(
            values = values,
            onClick = {
                showDialog = !showDialog
                onChange(it)
            }
        )
}

@Composable
fun FilterAlertDialog(values: List<String>, onClick: (String) -> Unit) {
    var show by remember {
        mutableStateOf(true)
    }
    DropdownMenu(expanded = show, onDismissRequest = { show = false }) {
        values.forEach {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(it) }) {
                DropdownMenuItem(
                    onClick = {
                        onClick(it)
                        show = false
                    }
                ) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
fun SearchTextField() {
    val viewModel: SearchViewModel = getViewModel()
    SearchComposable(
        label = "search a movie",
        onValueChange = {}
    ) {
        viewModel.setSearchResults(it)
    }
}

@Preview
@Composable
fun BoxContents() {
    MovieAppApiTheme {
        FilterBoxContents()
    }
}