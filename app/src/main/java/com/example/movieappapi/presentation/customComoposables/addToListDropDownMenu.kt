package com.example.movieappapi.presentation.customComoposables

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.movieappapi.domain.model.UserListsResponse
import com.example.movieappapi.presentation.screen.movie.MovieViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun UserListsDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit
) {
    val viewModel: MovieViewModel = getViewModel()
    val lists by viewModel.getUserLists().collectAsState(initial = UserListsResponse())
    DropdownMenu(expanded = expanded, onDismissRequest = { onDismissRequest() }) {
        lists.userLists?.forEach {
            DropdownMenuItem(
                onClick = {
                    onClick(it.id ?: 0)
                    onDismissRequest()
                }
            ) {
                Text(text = it.name ?: "")
            }
        }
    }
}