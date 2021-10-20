package com.example.movieappapi.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        topBar = { TopBarSetup() },
        bottomBar = { BottomBarSetup() },
        content = { NavHostScreen(navHostController = navHostController) }
    )
}

@Composable
fun BottomBarSetup() {

}

@Composable
fun TopBarSetup() {

}
