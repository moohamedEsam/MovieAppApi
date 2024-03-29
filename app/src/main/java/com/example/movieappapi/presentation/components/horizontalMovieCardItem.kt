package com.example.movieappapi.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.R
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.screen.home.navigateToMovieDetail
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HorizontalListMovieItem(movie: Movie, navHostController: NavHostController) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .padding(horizontal = 4.dp)
        ) {
            Card(modifier = Modifier
                .size(90.dp, 120.dp)
                .clickable {
                    navigateToMovieDetail(
                        movieId = movie.id ?: 0,
                        navHostController = navHostController
                    )
                }
            ) {
                Box {
                    Image(
                        painter = rememberImagePainter(
                            data = Url.getImageUrl(
                                movie.posterPath ?: ""
                            ),
                            builder = {
                                placeholder(R.drawable.icon_loading)
                                crossfade(true)
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = "%.1f".format(movie.voteAverage),
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.TopStart)
                            .background(Color.White),
                        color = Color.Black
                    )
                }
            }
            CreateVerticalSpacer(2.dp)
            Text(text = movie.title ?: "", maxLines = 2, fontWeight = FontWeight.Bold)
        }
    }
}