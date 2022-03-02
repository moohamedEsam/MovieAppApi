package com.example.movieappapi.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movieappapi.R
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.SemanticContentDescription
import com.example.movieappapi.presentation.screen.login.LoginViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun LoginScreen(navHostController: NavHostController) {
    val viewModel: LoginViewModel = getViewModel()
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
            onLoginClick = { username, password ->
                viewModel.login(username, password)
            },
            onStateSuccess = {
                navHostController.popBackStack()
                navHostController.navigate(Screens.MAIN)
            }
        )
        SignUpText(
            modifier = Modifier.Companion
                .align(Alignment.BottomStart)
                .clickable {
                    navHostController.navigate(Screens.REGISTER_SCREEN)
                }
        )
    }
}

@Composable
fun SignUpText(modifier: Modifier) {
    Text(
        text = buildAnnotatedString {
            append("don't have account? ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                append("sign up")
            }
        },
        modifier = modifier
    )
}

@ExperimentalAnimationApi
@Composable
fun LoginUi(
    modifier: Modifier,
    loginButtonText: String = "login",
    onLoginClick: (String, String) -> Unit,
    onStateSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = getViewModel()
    val userState by viewModel.userState
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        val username = userNameTextField()
        val password = passwordTextField()
        LoginButton(
            modifier = Modifier.Companion
                .align(End)
                .padding(8.dp),
            text = loginButtonText,
            onClick = { onLoginClick(username, password) },
            enabled = userState !is Resource.Loading
        )
        DividerRow()
        GoogleSignInButton()
        userState.HandleResourceChange {
            LaunchedEffect(key1 = Unit) {
                onStateSuccess()
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun LoginButton(
    modifier: Modifier,
    text: String = "login",
    onClick: () -> Unit,
    enabled: Boolean
) {
    val isVisible = remember {
        MutableTransitionState(enabled).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Button(
            onClick = {
                onClick()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            Text(text = text)
        }
    }

}

@Composable
fun GoogleSignInButton(
) {
    val viewModel: LoginViewModel = getViewModel()
    val activity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {

    }
    val authId = stringResource(id = R.string.authId)
    val context = LocalContext.current
    IconButton(
        onClick = {

        },
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}


@Composable
fun DividerRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Divider(modifier = Modifier.fillMaxWidth(0.4f))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "or")
        Spacer(modifier = Modifier.width(4.dp))
        Divider(modifier = Modifier.weight(1f))
    }
}


@ExperimentalAnimationApi
@Composable
fun userNameTextField(
    initialValue: String = "",
    title: String = "username"
): String {
    var value by remember {
        mutableStateOf(initialValue)
    }
    var isError by remember {
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
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it.trim()
                isError = it.isBlank() || it.contains(" ")
            },
            label = { Text(text = title) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .onFocusChanged {
                    focusColor = if (it.isFocused)
                        Color.Green
                    else
                        Color.DarkGray
                }
                .semantics {
                    contentDescription = SemanticContentDescription.LOGIN_SCREEN_USERNAME_TEXT_FIELD
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                leadingIconColor = focusColor
            )

        )
    }
    return value
}

@ExperimentalAnimationApi
@Composable
fun passwordTextField(): String {
    var value by remember {
        mutableStateOf("")
    }

    var isError by remember {
        mutableStateOf(false)
    }
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

        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it.trim()
                isError = it.isBlank() || it.length < 5 || it.contains(" ")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                if (visible)
                    IconButton(onClick = { visible = false }) {
                        Icon(imageVector = Icons.Outlined.VisibilityOff, contentDescription = null)
                    }
                else
                    IconButton(onClick = { visible = true }) {
                        Icon(imageVector = Icons.Outlined.Visibility, contentDescription = null)
                    }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .onFocusChanged {
                    focusColor = if (it.isFocused)
                        Color.Green
                    else
                        Color.DarkGray
                }
                .semantics {
                    contentDescription = SemanticContentDescription.LOGIN_SCREEN_PASSWORD_TEXT_FIELD
                },
            label = { Text(text = "password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (visible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                leadingIconColor = focusColor,
                trailingIconColor = focusColor
            )
        )
    }
    return value
}