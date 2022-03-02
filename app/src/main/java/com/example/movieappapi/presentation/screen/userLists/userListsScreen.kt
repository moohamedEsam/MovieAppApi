package com.example.movieappapi.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.UserList
import com.example.movieappapi.domain.model.UserListResponse
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.screen.userLists.UserListsViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun UserListsScreen(navHostController: NavHostController) {
    val viewModel: UserListsViewModel = getViewModel()
    val userList by viewModel.userLists
    userList.HandleResourceChange(
        onError = {
            if (userList.message == "user signed in as guest")
                SignInBox(userList, navHostController)
            else
                userList.ErrorStateComposable()
        }
    ) { userListResponse ->
        userListResponse.userLists?.let {
            LazyColumn(contentPadding = PaddingValues(8.dp), modifier = Modifier.fillMaxSize()) {
                items(it) {
                    UserMoviesList(userList = it)
                }
            }
        }
    }


}

@Composable
private fun SignInBox(
    userList: Resource<UserListResponse>,
    navHostController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color.Red)) {
                    append(userList.message ?: "")
                }
                withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("  sign in")
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navHostController.navigate(Screens.LOGIN) {
                        popUpTo(0) {
                            inclusive = false
                        }
                    }
                },
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@ExperimentalCoilApi
@Composable
fun UserMoviesList(userList: UserList) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = Url.getImageUrl(userList.posterPath ?: "")),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight(0.3f),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = userList.name ?: "",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp
            )
        }
    }
}