package com.example.movieappapi.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChipsListRow(
    values: List<String>,
    label: String,
    onClick: (Int) -> Unit
) {
    if (values.isEmpty()) return
    Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    ChipsListRow(values = values) {
        onClick(it)
    }
}

@Composable
private fun ChipsListRow(values: List<String>, onClick: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(values) { index, value ->
            OutlinedButton(onClick = {
                onClick(index)
            }, modifier = Modifier.padding(8.dp)) {
                Text(text = value)
            }
        }
    }
}