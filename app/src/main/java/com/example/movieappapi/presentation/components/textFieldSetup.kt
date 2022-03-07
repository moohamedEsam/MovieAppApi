package com.example.movieappapi.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldSetup(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: (String) -> Boolean = { it.isNotBlank() },
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    var isError by remember {
        mutableStateOf(error(text))
    }
    OutlinedTextField(
        value = text,
        label = { Text(text = label) },
        onValueChange = {
            onValueChange(it)
            isError = error(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        leadingIcon = {
            if (leadingIcon != null)
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onLeadingIconClick()
                    }
                )
        },
        trailingIcon = {
            if (trailingIcon != null)
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable {

                        onTrailingIconClick()
                    })
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        colors = colors
    )
}