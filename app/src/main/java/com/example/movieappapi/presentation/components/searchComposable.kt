package com.example.movieappapi.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SearchComposable(
    label: String,
    leadingIcon: ImageVector? = null,
    onLeadingIconClicked: () -> Unit = {},
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }

    var focusColor by remember {
        mutableStateOf(Color.DarkGray)
    }
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        trailingIcon = {
            IconButton(onClick = {
                onSearch(value.trim())
            }) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                focusColor = if (it.isFocused)
                    Color.Green
                else
                    Color.DarkGray
            },
        colors = TextFieldDefaults.textFieldColors(
            trailingIconColor = focusColor
        ),
        label = { Text(text = label) },
        leadingIcon = {
            if (leadingIcon != null)
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onLeadingIconClicked()
                    })
        }
    )
}