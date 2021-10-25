package com.example.movieappapi.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.dataModels.Movie
import com.example.movieappapi.utils.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun NavHostScreen(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.SPLASH
    ) {
        composable(Screens.SPLASH) {
            SplashWindow(navHostController = navHostController)
        }
        composable(Screens.LOGIN) {
            LoginScreen(navHostController = navHostController)
        }
        composable(Screens.MAIN) {
            MainFeed(navHostController)
        }

        composable(Screens.ACCOUNT_LISTS) {

        }
        composable(Screens.ACCOUNT) {

        }
        composable("${Screens.MOVIE_DETAILS}/{movie}", arguments = listOf(
            navArgument("movie") {
                type = NavType.StringType
            }
        )) {
            var movieString = it.arguments?.getString("movie", "") ?: ""
            movieString = movieString.replace("*", "/")
            val json = Json {
                isLenient = true
                ignoreUnknownKeys = true
            }
            val movie: Movie = json.decodeFromString(movieString)
            MovieDetails(movie = movie)
        }
    }

}