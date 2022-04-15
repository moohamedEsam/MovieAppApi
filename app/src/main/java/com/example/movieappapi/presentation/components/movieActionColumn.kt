package com.example.movieappapi.presentation.components

import androidx.compose.animation.Animatable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.presentation.customComoposables.RateMotionLayout
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import com.example.movieappapi.presentation.screen.movie.MovieViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MovieActionIconsColumn(movie: MovieDetailsResponse, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        FavoriteAction(movie)
        CreateVerticalSpacer()
        RateAction(movie)
        CreateVerticalSpacer()
        AddToWatchlistListAction()
    }
}

@Composable
private fun FavoriteAction(movie: MovieDetailsResponse) {
    val viewModel: MovieViewModel = getViewModel()
    val favoriteResponse by viewModel.favoriteResponse
    var favorite by remember {
        mutableStateOf(movie.accountStatesResponse?.favorite ?: false)
    }
    val color = remember { Animatable(if (favorite) Color.Red else Color.White) }
    LaunchedEffect(key1 = favoriteResponse) {
        favoriteResponse.onSuccess {
            favorite = favorite.not()
            color.animateTo(if (favorite) Color.Red else Color.White)
        }
    }
    IconButton(
        onClick = {
            viewModel.markAsFavorite()
        }
    ) {
        Icon(
            imageVector = if (favorite)
                Icons.Filled.Favorite
            else
                Icons.Outlined.Favorite,
            contentDescription = null,
            tint = color.value
        )
    }
}

@Composable
private fun RateAction(movie: MovieDetailsResponse) {
    val viewModel: MovieViewModel = getViewModel()
    val ratedResponse by viewModel.rateResponse
    var rated by remember {
        mutableStateOf(movie.accountStatesResponse?.rated?.isRated)
    }
    val color = remember {
        Animatable(if (rated == true) Color.Yellow else Color.White)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = ratedResponse) {
        ratedResponse.onSuccess {
            rated = it.statusCode == 1
            color.animateTo(if (rated == true) Color.Yellow else Color.White)
        }
    }
    if (showDialog)
        RateMotionLayout(
            initialProgress = movie.accountStatesResponse?.rated?.value ?: 0f,
            onDismissRequest = {
                showDialog = false
            },
            onRemoveRate = {
                viewModel.removeRate()
            },
            onRate = {
                viewModel.rateMovie(it)
            }
        )
    IconButton(onClick = {
        showDialog = true
    }) {
        Icon(
            imageVector = Icons.Filled.StarRate,
            contentDescription = null,
            tint = color.value
        )
    }
}

@Composable
fun AddToWatchlistListAction() {
    val viewModel: MovieViewModel = getViewModel()
    val watchlistResponse by viewModel.watchlistResponse
    val movie by viewModel.movie
    var watchList by remember {
        mutableStateOf(movie.data?.accountStatesResponse?.watchlist ?: false)
    }
    val color = remember {
        Animatable(if (watchList) Color.Yellow else Color.White)
    }
    LaunchedEffect(key1 = watchlistResponse) {
        watchlistResponse.onSuccess {
            watchList = watchList.not()
            color.animateTo(if (watchList) Color.Yellow else Color.White)
        }
    }
    IconButton(onClick = {
        viewModel.toggleAddToList()
    }) {
        Icon(
            imageVector = Icons.Default.WatchLater,
            contentDescription = null,
            tint = color.value
        )
    }
}
