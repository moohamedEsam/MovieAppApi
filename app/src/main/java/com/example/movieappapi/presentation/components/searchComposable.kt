package com.example.movieappapi.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
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

    StateFullSearchComposable(
        value = value,
        onValueChange = {
            onValueChange(it)
            value = it
        },
        onSearch = onSearch,
        label = label,
        leadingIcon = leadingIcon,
        onLeadingIconClicked = onLeadingIconClicked
    )
}

@Composable
fun StateFullSearchComposable(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    onLeadingIconClicked: () -> Unit = {}
) {
    var focusColor by remember {
        mutableStateOf(Color.DarkGray)
    }
    TextField(
        value = value,
        onValueChange = {
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
        },
        keyboardActions = KeyboardActions(onDone = {
            onSearch(value)
        }),
        maxLines = 1,
        singleLine = true
    )
}