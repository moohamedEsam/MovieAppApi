package com.example.movieappapi.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieappapi.utils.Screens

@Composable
fun NavHostScreen(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.LOGIN) {
        composable(Screens.SPLASH) {
            SplashWindow(navHostController = navHostController)
        }
        composable(Screens.LOGIN) {
            LoginScreen(navHostController = navHostController)
        }
        composable(Screens.MAIN) {

        }
    }

}