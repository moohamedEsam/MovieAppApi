package com.example.movieappapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.presentation.screen.MainScreen
import com.example.movieappapi.presentation.screen.login.LoginViewModel
import com.example.movieappapi.ui.theme.MovieAppApiTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by inject()
    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalCoilApi::class,
        ExperimentalPagerApi::class,
        ExperimentalSerializationApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.userAlreadyLoggedIn()
                viewModel.userState.value is Resource.Success || viewModel.userState.value is Resource.Error
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val userState by viewModel.userState
                    when (userState) {
                        is Resource.Success, is Resource.Error -> {
                            MainScreen(
                                if (userState is Resource.Success)
                                    Screens.MAIN
                                else
                                    Screens.LOGIN
                            )
                        }
                        else -> Unit
                    }

                }
            }
        }
    }
}

