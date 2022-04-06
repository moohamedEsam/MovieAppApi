package com.example.movieappapi.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.customComoposables.HorizontalMovieListWithLabel
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import com.example.movieappapi.presentation.screen.recommendation.MovieRecommendationsViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SimilarMovieScreen(movieId: Int, posterPath: String, navHostController: NavHostController) {
    val viewModel: MovieRecommendationsViewModel = getViewModel()
    val recommendations by viewModel.recommendations
    val similar by viewModel.similarMovies
    LaunchedEffect(key1 = Unit) {
        viewModel.setRecommendations(movieId)
        viewModel.setSimilarMovies(movieId)
    }
    BoxWithConstraints {
        val height = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberImagePainter(data = Url.getImageUrl("/$posterPath")),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height.div(2.5f)),
                contentScale = ContentScale.FillBounds
            )
            CreateVerticalSpacer()
            HorizontalMovieListWithLabel(similar, navHostController, "SimilarMovies") {
                viewModel.setSimilarMovies(movieId)
            }
            CreateVerticalSpacer()
            HorizontalMovieListWithLabel(
                moviesResponse = recommendations,
                navHostController = navHostController,
                label = "Recommendations"
            ) {
                viewModel.setRecommendations(movieId)
            }
        }
    }
}
