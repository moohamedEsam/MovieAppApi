package com.example.movieappapi.domain.utils

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
    fun ErrorStateComposable(modifier: Modifier = Modifier) {
        Text(
            text = message ?: "",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = modifier.semantics {
                contentDescription = SemanticContentDescription.ERROR_RESOURCE_TEXT
            }
        )
    }

    @Composable
    private fun LoadingStateComposable(modifier: Modifier = Modifier) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary, modifier = modifier)
    }

    suspend fun onSuccess(perform: suspend (T) -> Unit) {
        if (this is Success && data != null)
            perform(data!!)
    }
}


