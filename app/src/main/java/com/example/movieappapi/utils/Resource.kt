package com.example.movieappapi.utils

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
    open fun DisplayComposable(code: @Composable () -> Unit = {}) {
        if (this !is Initialized)
            code()
    }

    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message) {
        @Composable
        override fun DisplayComposable(code: @Composable () -> Unit) {
            Text(
                text = message ?: "",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics {
                    contentDescription = SemanticContentDescription.ERROR_RESOURCE_TEXT
                })
        }
    }

    class Loading<T> : Resource<T>() {
        @Composable
        override fun DisplayComposable(code: @Composable () -> Unit) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    }

    class Initialized<T> : Resource<T>()
}
