package com.example.movieappapi.composables

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movieappapi.R
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Screens
import com.example.movieappapi.viewModels.LoginViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashWindow(navHostController: NavHostController) {
    val viewModel: LoginViewModel = getViewModel()
    val user by viewModel.userState
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = Unit) {
        viewModel.checkPreviousLogin(activity = activity)
        navHostController.popBackStack()
        if (user is Resource.Success)
            navHostController.navigate(Screens.MAIN)
        else
            navHostController.navigate("${Screens.LOGIN}/${true}")
        viewModel.reinitializeUserState()
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