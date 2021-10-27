package com.example.movieappapi.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight

sealed class Resource<T>(var data: T? = null, var message: String? = null) {

    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Loading<T> : Resource<T>()

    class Initialized<T> : Resource<T>()
}


@Composable
fun <T> HandleResourceChange(
    state: Resource<T>,

    onLoading: @Composable () -> Unit = {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    },

    onError: @Composable () -> Unit = {
        Text(
            text = state.message ?: "",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics {
                contentDescription = SemanticContentDescription.ERROR_RESOURCE_TEXT
            }
        )
    },

    onSuccess: @Composable () -> Unit
) {
    when (state) {
        is Resource.Success -> onSuccess()
        is Resource.Loading -> onLoading()
        is Resource.Error -> onError()
        else -> Unit
    }
}