package com.example.movieappapi.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movieappapi.R
import com.example.movieappapi.utils.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashWindow(navHostController: NavHostController) {
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navHostController.navigate(Screens.LOGIN)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
    }
}