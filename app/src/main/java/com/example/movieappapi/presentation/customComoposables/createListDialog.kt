package com.example.movieappapi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CreateListDialog(
    onDismissRequest: () -> Unit,
    onDone: (String, String) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(8.dp)
        ) {
            val name = textFieldSetup(label = "name")
            val description = textFieldSetup(label = "description  ")
            Button(
                onClick = {
                    onDone(name, description)
                    onDismissRequest()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "create")
            }
        }
    }
}

@Composable
fun textFieldSetup(label: String): String {
    var value by remember {
        mutableStateOf("")
    }
    TextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    return value
}