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

    @Composable
    fun HandleResourceChange(
        onLoading: @Composable () -> Unit = { LoadingStateComposable() },
        onError: @Composable () -> Unit = { ErrorStateComposable() },
        onSuccess: @Composable (T) -> Unit
    ) {
        when (this) {
            is Success -> {
                data?.let { onSuccess(it) }
            }
            is Loading -> onLoading()
            is Error -> onError()
            else -> Unit
        }
    }

    @Composable
    fun ErrorStateComposable() {
        Text(
            text = message ?: "",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics {
                contentDescription = SemanticContentDescription.ERROR_RESOURCE_TEXT
            }
        )
    }

    @Composable
    fun LoadingStateComposable() {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    }

    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Loading<T> : Resource<T>()

    class Initialized<T> : Resource<T>()
}


