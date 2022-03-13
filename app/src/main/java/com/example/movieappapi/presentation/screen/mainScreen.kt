package com.example.movieappapi.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.presentation.navigation.NavHostScreen
import com.example.movieappapi.ui.theme.MovieAppApiTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalAnimationApi
@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainScreen(startDestination: String) {
    val navHostController = rememberNavController()
    val navBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination?.route?.split("/")?.get(0)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (
                currentDestination !in listOf(
                    Screens.LOGIN,
                    Screens.MOVIE_DETAILS,
                    null
                )
            )
                BottomBarSetup(navHostController = navHostController)
        },
        content = { NavHostScreen(navHostController = navHostController, startDestination) },
        drawerContent = { DrawerContent(navHostController) }
    )
}

@Composable
fun ColumnScope.DrawerContent(navHostController: NavHostController) {
    val navBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination?.route
    DrawerItem(
        label = Screens.ACCOUNT_Favorite_Movies,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Favorite_Movies)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Favorite_Movies)
    }

    DrawerItem(
        label = Screens.ACCOUNT_Favorite_Tv,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Favorite_Tv)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Favorite_Tv)
    }

    DrawerItem(
        label = Screens.ACCOUNT_Rated_Movies,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.StarRate,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Rated_Movies)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Rated_Movies)
    }

    DrawerItem(
        label = Screens.ACCOUNT_Rated_Tv,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.StarRate,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Rated_Tv)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Rated_Tv)
    }

    DrawerItem(
        label = Screens.ACCOUNT_Watchlist_Movies,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.WatchLater,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Watchlist_Movies)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Watchlist_Movies)
    }

    DrawerItem(
        label = Screens.ACCOUNT_Watchlist_TV,
        currentDestination = currentDestination,
        icon = {
            Icon(
                imageVector = Icons.Default.WatchLater,
                contentDescription = null,
                tint = if (currentDestination == Screens.ACCOUNT_Watchlist_TV)
                    MaterialTheme.colors.primary
                else
                    Color.White
            )
        }
    ) {
        navHostController.navigate(Screens.ACCOUNT_Watchlist_TV)
    }

}

@Composable
fun DrawerItem(
    label: String,
    currentDestination: String?,
    enabled: Boolean = label == currentDestination,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = if (enabled) MaterialTheme.colors.primary else Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
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

@Preview
@Composable
fun BottomAppBarPreview() {
    MovieAppApiTheme {

        val navHostController = rememberNavController()
        Column {
            DrawerContent(navHostController = navHostController)
        }
    }
}
