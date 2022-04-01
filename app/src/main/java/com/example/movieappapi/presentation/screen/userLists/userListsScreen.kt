package com.example.movieappapi.presentation.screen.userLists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.UserList
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.domain.utils.UserStatus
import com.example.movieappapi.presentation.components.CreateListDialog
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun UserListsScreen(navHostController: NavHostController) {
    val viewModel: UserListsViewModel = getViewModel()
    val userStatus by viewModel.userStatus
    if (userStatus !is UserStatus.LoggedIn)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
    else
        UserCreatedListsUi(navHostController = navHostController)
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserCreatedListsUi(navHostController: NavHostController) {
    val viewModel: UserListsViewModel = getViewModel()
    val userLists by viewModel.userLists
    LaunchedEffect(key1 = Unit) {
        viewModel.setUserLists()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        userLists.OnSuccessComposable {
            it.userLists?.let { list: List<UserList> ->
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(list) { userList ->
                        UserListUI(userList = userList, navHostController)
                    }
                }
            }
        }
        CreateListButton(modifier = Modifier.align(BottomEnd))
        ResourceErrorSnackBar(resource = userLists) {
            viewModel.setUserLists()
        }
    }
}

@Composable
fun CreateListButton(modifier: Modifier = Modifier) {
    val viewModel: UserListsViewModel = getViewModel()
    var showDialog by remember {
        mutableStateOf(false)
    }
    if (showDialog)
        CreateListDialog(onDismissRequest = { showDialog = false }) { name, description ->
            viewModel.createList(name, description)
        }
    FloatingActionButton(
        onClick = {
            showDialog = true
        },
        modifier = modifier.padding(bottom = 48.dp, end = 8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary
        )
    }
}


@ExperimentalCoilApi
@Composable
fun UserListUI(userList: UserList, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navHostController.navigate("${Screens.LIST}/${userList.id ?: 0}")
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!userList.posterPath.isNullOrEmpty())
                Image(
                    painter = rememberImagePainter(
                        data = Url.getImageUrl(userList.posterPath!!)
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(0.3f),
                    contentScale = ContentScale.FillBounds
                )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = userList.name ?: "",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp
                )

                Text(text = userList.description.toString())
                Text(text = "${userList.itemCount} items")
            }
        }
    }
}