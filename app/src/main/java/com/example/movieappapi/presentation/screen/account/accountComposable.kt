package com.example.movieappapi.presentation.screen.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.UserStatus
import org.koin.androidx.compose.getViewModel

@Composable
fun AccountScreen(
    navHostController: NavHostController
) {
    val viewModel: AccountViewModel = getViewModel()
    val context = LocalContext.current
    val userStatus by viewModel.userStatus
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Log Out", modifier = Modifier.clickable {
            viewModel.logOut()
        })
        if (userStatus is UserStatus.LoggedOut)
            LaunchedEffect(key1 = Unit) {
                navHostController.navigate(Screens.LOGIN) {
                    popUpTo(Screens.MAIN) {
                        inclusive = true
                    }
                }
            }


    }
}