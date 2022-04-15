package com.example.movieappapi.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.presentation.screen.movie.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MovieCardDescription(
    background: Color,
    movie: MovieDetailsResponse,
    navHostController: NavHostController
) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    var cardVisible by remember {
        mutableStateOf(true)
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(2000)) + fadeIn(
            animationSpec = tween(2000)
        ),
        modifier = Modifier.animateContentSize()

    ) {
        Card(
            backgroundColor = background,
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            modifier = Modifier
                .alpha(0.9f)
                .draggable(
                    state = rememberDraggableState {
                        cardVisible = it < 0
                    },
                    orientation = Orientation.Vertical
                )
        ) {
            if (cardVisible)
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .draggable(
                                state = rememberDraggableState {
                                    cardVisible = it < 0
                                },
                                orientation = Orientation.Vertical
                            )
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                cardVisible = false
                            }
                        ) {
                            Icon(imageVector = Icons.Default.ExpandLess, contentDescription = null)
                        }
                    }
                    Text(
                        text = movie.title ?: "",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    CreateVerticalSpacer(4.dp)
                    KeywordsChips(movie) {
                        val keyword = movie.keywords?.keywords?.getOrNull(it)
                        if (keyword != null) {
                            val id = keyword.id ?: 0
                            val params: HashMap<String, String> = HashMap(
                                mapOf(
                                    "with_keywords" to id.toString()
                                )
                            )
                            val paramsString = Json.encodeToString(params)
                            navHostController.navigate("${Screens.DISCOVER_SCREEN}/$paramsString")
                        }
                    }
                    CreateVerticalSpacer(4.dp)
                    GenreChips(movie) {
                        val genre = movie.genres?.getOrNull(it)
                        if (genre != null) {
                            val id = genre.id ?: 0
                            val params: HashMap<String, String> = HashMap(
                                mapOf(
                                    "with_genres" to id.toString()
                                )
                            )
                            val paramsString = Json.encodeToString(params)
                            navHostController.navigate("${Screens.DISCOVER_SCREEN}/$paramsString")
                        }
                    }
                    CreateVerticalSpacer(dp = 4.dp)
                    movie.movieCredits?.cast?.let {
                        CastList(it, navHostController)
                    }
                    CreateVerticalSpacer(4.dp)
                    Text(text = movie.overview ?: "")
                    CreateVerticalSpacer(4.dp)
                    SimilarMoviesButton(navHostController, movie)
                }
            else
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .draggable(
                            state = rememberDraggableState {
                                cardVisible = it < 0
                            },
                            orientation = Orientation.Vertical
                        )
                ) {
                    IconButton(
                        onClick = { cardVisible = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                    }
                }

        }
    }
}