package com.example.movieappapi.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Screens
import com.example.movieappapi.utils.SemanticContentDescription
import com.example.movieappapi.viewModels.LoginViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LoginColumn(
            navHostController = navHostController,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        Text(
            text = buildAnnotatedString {
                append("don't have account? ")
                withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("sign up")
                }
            },
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun LoginColumn(navHostController: NavHostController, modifier: Modifier) {
    val viewModel: LoginViewModel = getViewModel()
    val userState by viewModel.userState
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        val username = userNameTextField()
        Spacer(modifier = Modifier.height(8.dp))
        val password = passwordTextField()
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (userState !is Resource.Loading)
                    viewModel.signIn(Credentials(username, password))
            },
            modifier = Modifier.align(End)
        ) {
            Text(text = "login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "or sign in as guest",
            modifier = Modifier.clickable { viewModel.signInAsGuest() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        userState.DisplayComposable {
            Log.d("loginScreen", "LoginColumn: called")
            navHostController.navigate(Screens.MAIN)
            viewModel.reinitializeUserState()
        }
    }
}

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

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it.trim()
            isError = it.isBlank()
        },
        label = { Text(text = title) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = SemanticContentDescription.LOGIN_SCREEN_USERNAME_TEXT_FIELD
            }

    )
    return value
}

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

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it.trim()
            isError = it.isBlank() || it.length < 5
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
            .semantics {
                contentDescription = SemanticContentDescription.LOGIN_SCREEN_PASSWORD_TEXT_FIELD
            },
        label = { Text(text = "password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        isError = isError
    )
    return value
}