package com.example.movieappapi.composables

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movieappapi.presentation.screen.login.LoginViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun SignUpScreen(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val viewModel: LoginViewModel = getViewModel()
        val context = LocalContext.current
        LoginUi(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            loginButtonText = "sign up",
            onLoginClick = { username, password ->

            }
        ) {
            Toast.makeText(
                context,
                "account created check your email for authentication",
                Toast.LENGTH_LONG
            ).show()
            navHostController.popBackStack()
        }

        Text(
            text = buildAnnotatedString {
                append("already have an account? ")
                withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("sign in")
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navHostController.popBackStack()
                }
                .align(Alignment.BottomStart)
        )
    }
}