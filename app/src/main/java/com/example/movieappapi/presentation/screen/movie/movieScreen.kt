package com.example.movieappapi.composables

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.screen.movie.MovieViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun MovieDetails(movie: Movie, navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
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
        LikeIcon(
            movie = movie, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun MovieDescription(
    movie: Movie,
    modifier: Modifier,
    navHostController: NavHostController
) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    val viewModel: MovieViewModel = getViewModel()
    val genres by viewModel.getMovieGenres(movie.genreIds ?: emptyList())
        .collectAsState(emptyList())
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
                GenreChipsList(genres = genres)
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
fun LikeIcon(movie: Movie, modifier: Modifier) {
    val viewModel: MovieViewModel = getViewModel()
    val isLiked by viewModel.isLiked
    LaunchedEffect(key1 = Unit) {

    }
    if (isLiked)
        IconButton(
            onClick = {

            },
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.Red
            )
        }
    else
        IconButton(
            onClick = {

            },
            modifier = modifier
        ) {
            Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null)
        }

}

@Composable
fun CreateVerticalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.height(dp))
}

@Preview
@Composable
fun ChipsPreview() {
    GenreChipsList(genres = listOf("action", "drama", "horror"))
}