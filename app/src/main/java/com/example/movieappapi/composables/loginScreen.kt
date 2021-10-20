package com.example.movieappapi.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.movieappapi.R

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LoginColumn(
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
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun LoginColumn(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        userNameTextField()
        Spacer(modifier = Modifier.height(8.dp))
        passwordTextField()
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
            value = it
            isError = it.isBlank()
        },
        label = { Text(text = title) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()

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
            value = it
            isError = it.isBlank() && it.length < 5
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
            .fillMaxWidth(),
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