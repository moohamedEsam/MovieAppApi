package com.example.movieappapi.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun SearchComposable(
    onValueChange: (String) -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }
    TextField(
        value = query,
        onValueChange = {
            query = it
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "search movies") }
    )
}