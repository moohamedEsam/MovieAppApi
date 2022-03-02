package com.example.movieappapi.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.utils.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalAnimationApi
@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    val navBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination?.route?.split("/")?.get(0)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (
                currentDestination !in listOf(
                    Screens.LOGIN,
                    Screens.SPLASH,
                    Screens.MOVIE_DETAILS,
                    Screens.REGISTER_SCREEN
                ) &&
                currentDestination != null
            )
                BottomBarSetup(navHostController = navHostController)
        },
        content = { NavHostScreen(navHostController = navHostController) }
    )
}

@Composable
fun BottomBarSetup(navHostController: NavHostController) {
    val navBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination?.route
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.secondary,
        elevation = 0.dp
    ) {
        NavItemSetup(
            currentDestination = currentDestination,
            navHostController = navHostController,
            itemRoute = Screens.MAIN,
            itemIcon = Icons.Outlined.Home,
            itemLabel = "home"
        )
        NavItemSetup(
            currentDestination = currentDestination,
            navHostController = navHostController,
            itemRoute = Screens.SEARCH_SCREEN,
            itemIcon = Icons.Outlined.Search,
            itemLabel = "search"
        )
        NavItemSetup(
            currentDestination = currentDestination,
            navHostController = navHostController,
            itemRoute = Screens.ACCOUNT_LISTS,
            itemIcon = Icons.Default.Menu,
            itemLabel = "lists"
        )
        NavItemSetup(
            currentDestination = currentDestination,
            navHostController = navHostController,
            itemRoute = Screens.ACCOUNT,
            itemIcon = Icons.Outlined.Person,
            itemLabel = "profile"
        )
    }
}

@Composable
private fun RowScope.NavItemSetup(
    currentDestination: String?,
    navHostController: NavHostController,
    itemRoute: String,
    itemIcon: ImageVector,
    itemLabel: String
) {
    BottomNavigationItem(
        selected = currentDestination == itemRoute,
        onClick = {
            navHostController.navigate(itemRoute) {
                launchSingleTop = true
                popUpTo(0) {
                    inclusive = false
                }
            }
        },
        icon = {
            Icon(
                imageVector = itemIcon,
                contentDescription = null
            )
        },
        label = { Text(text = itemLabel) },
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = MaterialTheme.colors.secondary
    )
}

/*
@Preview
@Composable
fun BottomAppBarPreview() {
    MovieAppApiTheme {

        val navHostController = rememberNavController()
        BottomBarSetup(navHostController = navHostController)
    }
}*/
