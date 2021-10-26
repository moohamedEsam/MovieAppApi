package com.example.movieappapi.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Url
import com.example.movieappapi.viewModels.MovieRecommendationsViewModel
import org.koin.androidx.compose.getViewModel

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
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberImagePainter(data = Url.getImageUrl("/$posterPath")),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            contentScale = ContentScale.FillBounds
        )
        CreateVerticalSpacer()
        SimilarMovies(similar, navHostController)
        CreateVerticalSpacer()
        RecommendationsMovies(
            recommendations = recommendations,
            navHostController = navHostController
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun SimilarMovies(
    similar: Resource<MoviesResponse>,
    navHostController: NavHostController
) {
    Text(
        text = "similar movies",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    similar.data?.results?.let {
        Log.d("similarMoviesScreen", "SimilarMovies: ${it.size}")
        HorizontalMovieList(movies = it, navHostController = navHostController)
    }
}

@ExperimentalCoilApi
@Composable
private fun RecommendationsMovies(
    recommendations: Resource<MoviesResponse>,
    navHostController: NavHostController
) {
    Text(
        text = "recommendations",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    recommendations.data?.results?.let {
        HorizontalMovieList(movies = it, navHostController = navHostController)
    }
}