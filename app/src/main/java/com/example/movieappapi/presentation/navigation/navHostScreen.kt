package com.example.movieappapi.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.composables.MainFeed
import com.example.movieappapi.composables.SearchScreen
import com.example.movieappapi.composables.SimilarMovieScreen
import com.example.movieappapi.composables.UserListsScreen
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.presentation.screen.account.AccountScreen
import com.example.movieappapi.presentation.screen.login.LoginScreen
import com.example.movieappapi.presentation.screen.movie.MovieDetails
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@ExperimentalAnimationApi
@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun NavHostScreen(navHostController: NavHostController, startDestination: String) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Screens.LOGIN) {
            LoginScreen(navHostController = navHostController)
        }

        composable(Screens.MAIN) {
            MainFeed(navHostController)
        }

        composable(Screens.SEARCH_SCREEN) {
            SearchScreen(navHostController = navHostController)
        }

        composable(Screens.ACCOUNT_LISTS) {
            UserListsScreen(navHostController = navHostController)
        }
        composable(Screens.ACCOUNT) {
            AccountScreen(navHostController = navHostController)
        }
        composable(
            "${Screens.MOVIE_DETAILS}/{movie}", arguments = listOf(
                navArgument("movie") {
                    type = NavType.StringType
                }
            )) {
            val movie: Movie = getMovieFromStack(it)
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

        composable(Screens.ACCOUNT_Favorite_Movies) {}
        composable(Screens.ACCOUNT_Favorite_Tv) {}
        composable(Screens.ACCOUNT_Rated_Movies) {}
        composable(Screens.ACCOUNT_Rated_Tv) {}
        composable(Screens.ACCOUNT_Watchlist_Movies) {}
        composable(Screens.ACCOUNT_Watchlist_TV) {}
    }

}

@Composable
fun getMovieFromStack(it: NavBackStackEntry): Movie {
    var movieString = it.arguments?.getString("movie", "") ?: ""
    movieString = movieString.replace("*", "/")
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    return json.decodeFromString(movieString)
}