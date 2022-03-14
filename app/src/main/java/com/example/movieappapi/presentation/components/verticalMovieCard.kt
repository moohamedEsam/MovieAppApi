package com.example.movieappapi.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.Result
import com.example.movieappapi.domain.utils.Url


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SearchItem(result: Result) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = slideOutHorizontally(animationSpec = tween(1000))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(4.dp, Color.Black))
                .padding(8.dp)
        ) {
            Row {
                Image(
                    painter = rememberImagePainter(
                        data = Url.getImageUrl(
                            result.posterPath ?: result.profilePath ?: ""
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp, 120.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = result.originalTitle ?: result.name ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun VerticalCardMovieItem(result: Movie) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = slideOutHorizontally(animationSpec = tween(1000))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(4.dp, Color.Black))
                .padding(8.dp)
        ) {
            Row {
                Image(
                    painter = rememberImagePainter(
                        data = Url.getImageUrl(
                            result.posterPath ?: ""
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp, 120.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = result.originalTitle ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
