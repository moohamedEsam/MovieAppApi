package com.example.movieappapi.presentation.screen.login

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movieappapi.R
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import com.example.movieappapi.presentation.components.TextFieldSetup
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun LoginScreen(navHostController: NavHostController) {
    val viewModel: LoginViewModel = getViewModel()
    val userState by viewModel.userState
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LoginUi(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            navHostController = navHostController
        )
        Column(modifier = Modifier.align(BottomCenter)) {
            ContinueAsGuest()
            CreateVerticalSpacer()
            SignUp(navHostController)

        }
        ResourceErrorSnackBar(resource = userState, "") {}
    }
}

@Composable
fun SignUp(navHostController: NavHostController) {
    Text(
        text = "Sign up",
        color = MaterialTheme.colors.primary,
        modifier = Modifier.clickable {
            navHostController.navigate(Screens.SIGN_UP)
        }
    )
}

@Composable
fun ContinueAsGuest() {
    val viewModel: LoginViewModel = getViewModel()
    Text(
        text = "Continue as Guest",
        color = MaterialTheme.colors.primary,
        modifier = Modifier.clickable {
            viewModel.loginAsGuest()
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun LoginUi(
    modifier: Modifier,
    navHostController: NavHostController
) {
    val viewModel: LoginViewModel = getViewModel()
    val userState by viewModel.userState
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        UserNameTextField()
        PasswordTextField()
        LoginButton(
            modifier = Modifier
                .align(End)
                .padding(8.dp)
        )
        userState.OnSuccessComposable {
            LaunchedEffect(key1 = Unit) {
                navHostController.popBackStack()
                navHostController.navigate(Screens.MAIN)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun LoginButton(
    modifier: Modifier
) {
    val viewModel: LoginViewModel = getViewModel()
    val userState by viewModel.userState
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Button(
            onClick = {
                viewModel.login()
            },
            modifier = modifier,
            enabled = isVisible.currentState,
        ) {
            Text(text = "Login")
        }
        if (userState is Resource.Error)
            isVisible.targetState = true
    }

}

@ExperimentalAnimationApi
@Composable
fun UserNameTextField(

) {
    val viewModel: LoginViewModel = getViewModel()
    val value by viewModel.username

    var focusColor by remember {
        mutableStateOf(Color.DarkGray)
    }

    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = expandHorizontally(expandFrom = Alignment.Start, animationSpec = tween(1000)),
        exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
    ) {
        TextFieldSetup(
            text = value,
            label = "username",
            onValueChange = {
                viewModel.setUsername(it)
            },
            error = { it.isBlank() || it.contains(" ") },
            modifier = Modifier
                .onFocusChanged {
                    focusColor = if (it.isFocused)
                        Color.Green
                    else
                        Color.DarkGray
                },
            leadingIcon = Icons.Default.Email,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                leadingIconColor = focusColor
            )
        )

    }

}

@ExperimentalAnimationApi
@Composable
fun PasswordTextField() {
    val viewModel: LoginViewModel = getViewModel()
    val value by viewModel.password

    var visible by remember {
        mutableStateOf(false)
    }

    var focusColor by remember {
        mutableStateOf(Color.DarkGray)
    }

    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = expandHorizontally(expandFrom = Alignment.Start, animationSpec = tween(1000)),
        exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
    ) {
        TextFieldSetup(
            text = value,
            label = "password",
            onValueChange = {
                viewModel.setPassword(it)
            },
            modifier = Modifier.onFocusChanged {
                focusColor = if (it.isFocused)
                    Color.Green
                else
                    Color.DarkGray
            },
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (visible)
                Icons.Outlined.VisibilityOff
            else
                Icons.Outlined.Visibility,
            onTrailingIconClick = {
                visible = !visible
            },
            visualTransformation = if (visible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            error = { it.isBlank() || it.length < 5 || it.contains(" ") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                leadingIconColor = focusColor,
                trailingIconColor = focusColor
            )
        )
    }
}




