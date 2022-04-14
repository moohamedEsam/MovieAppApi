package com.example.movieappapi.presentation.customComoposables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HorizontalMovieListWithLabel(
    moviesResponse: Resource<MoviesResponse>,
    navHostController: NavHostController,
    label: String,
    paginate: () -> Unit
) {

    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
    CreateVerticalSpacer(dp = 4.dp)
    moviesResponse.OnSuccessComposable {
        it.results?.let { movies ->
            HorizontalMovieList(movies = movies, navHostController = navHostController) {
                paginate()
            }
        }
    }

}