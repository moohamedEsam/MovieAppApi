package com.example.movieappapi.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.movieappapi.presentation.screen.discover.DiscoverViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiscoverFilterDialog(
    onDismissRequest: () -> Unit
) {
    val viewModel: DiscoverViewModel = getViewModel()
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                val language = filterEntry("Language")
                val region = filterEntry("Region")
                val sortBy = filterEntry("Sort by")
                val year = filterEntry("Year")


                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(.8f))
                    Button(onClick = {
                        viewModel.addFilter {
                            it["language"] = language
                            it["region"] = region
                            it["sort_by"] = sortBy
                            it["year"] = year
                            it
                        }
                        onDismissRequest()
                    }) {
                        Text(text = "Apply")
                    }
                }
            }
        }
    }
}

@Composable
private fun filterEntry(label: String): String {
    var value by remember {
        mutableStateOf("")
    }
    TextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(text = label) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
    return value
}