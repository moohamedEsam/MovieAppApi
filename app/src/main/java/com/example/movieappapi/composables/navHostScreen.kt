package com.example.movieappapi.composables

import androidx.compose.runtime.Composable
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
            MovieDetails(movie = movie, navHostController = navHostController)
        }
        composable(
            "${Screens.SIMILAR_MOVIES_SCREEN}/{movieId}/{posterPath}",
            arguments = listOf(
                navArgument("movieId")
                {
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("posterPath") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) {
            val movieId = it.arguments?.getInt("movieId", 1) ?: 1
            val posterPath = it.arguments?.getString("posterPath", " ") ?: ""
            SimilarMovieScreen(
                movieId = movieId,
                posterPath = posterPath,
                navHostController = navHostController
            )
        }
    }

}