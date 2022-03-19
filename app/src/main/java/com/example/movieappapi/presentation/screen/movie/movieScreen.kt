package com.example.movieappapi.presentation.screen.movie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.components.RateMotionLayout
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun MovieDetails(movieId: Int, navHostController: NavHostController) {
    val viewModel: MovieViewModel = getViewModel()
    val movie by viewModel.movie
    LaunchedEffect(key1 = Unit) {
        viewModel.setMovie(movieId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        movie.HandleResourceChange {
            movie.data?.let { data -> MovieUi(data, navHostController) }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun BoxScope.MovieUi(
    movie: MovieDetailsResponse,
    navHostController: NavHostController
) {

    Image(
        painter = rememberImagePainter(data = Url.getImageUrl(movie.posterPath ?: "")),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    MovieDescription(
        movie = movie,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .align(Alignment.BottomStart),
        navHostController = navHostController
    )
    MovieActionIconsColumn(movie, Modifier.align(Alignment.TopEnd))

}

@Composable
private fun MovieActionIconsColumn(movie: MovieDetailsResponse, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        val viewModel: MovieViewModel = getViewModel()
        var favorite by remember {
            mutableStateOf(movie.accountStatesResponse?.favorite ?: false)
        }

        val rated = movie.accountStatesResponse?.rated?.isRated ?: false
        IconButton(onClick = {
            viewModel.markAsFavorite()
            favorite = favorite.not()
        }) {
            Icon(
                imageVector = if (favorite)
                    Icons.Filled.Favorite
                else
                    Icons.Outlined.Favorite, contentDescription = null,
                tint = if (favorite)
                    Color.Red
                else
                    Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RateAction(rated)
    }
}

@Composable
private fun RateAction(rated: Boolean, value: Float? = null) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val viewModel: MovieViewModel = getViewModel()
    if (showDialog)
        RateMotionLayout(
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
            tint = if (rated)
                Color.Yellow
            else
                Color.White
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun MovieDescription(
    movie: MovieDetailsResponse,
    modifier: Modifier,
    navHostController: NavHostController
) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    val viewModel: MovieViewModel = getViewModel()
    AnimatedVisibility(
        visibleState = isVisible,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(2000)) + fadeIn(
            animationSpec = tween(2000)
        ),
        modifier = modifier
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = movie.title ?: "",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.StarRate, contentDescription = null)
                        Text(text = "${movie.voteAverage}")
                    }
                }
                CreateVerticalSpacer(4.dp)
                GenreChipsList(genres = movie.genres?.mapNotNull { it.name } ?: emptyList())
                CreateVerticalSpacer(dp = 4.dp)
                Text(text = movie.overview ?: "")
                CreateVerticalSpacer(4.dp)
                OutlinedButton(
                    onClick = {
                        navHostController.navigate(
                            "${Screens.SIMILAR_MOVIES_SCREEN}/${movie.id ?: 1}/${
                                movie.posterPath?.substring(
                                    1,
                                    movie.posterPath?.length ?: 1
                                )
                            }"
                        )
                    }
                ) {
                    Text(text = "see similar movies")
                }
            }
        }
    }
}

@Composable
fun GenreChipsList(genres: List<String>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(genres) {
            Text(
                text = it,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}


@Composable
fun CreateVerticalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.height(dp))
}

