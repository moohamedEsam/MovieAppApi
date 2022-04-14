package com.example.movieappapi.presentation.screen.movie

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.movieappapi.R
import com.example.movieappapi.domain.model.Cast
import com.example.movieappapi.domain.model.MovieDetailsResponse
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.components.getPalette
import com.example.movieappapi.presentation.customComoposables.RateMotionLayout
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun MovieDetails(movieId: Int, navHostController: NavHostController) {
    val viewModel: MovieViewModel = getViewModel()
    val movie by viewModel.movie
    LaunchedEffect(key1 = Unit) {
        viewModel.setMovie(movieId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        movie.OnSuccessComposable {
            MovieUi(movie = it, navHostController = navHostController)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoilApi::class)
@Composable
private fun BoxScope.MovieUi(
    movie: MovieDetailsResponse,
    navHostController: NavHostController
) {
    var image: Bitmap? by remember {
        mutableStateOf(null)
    }
    var background by remember {
        mutableStateOf(Color.Black)
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        movie.posterPath?.let {
            image = getPalette(it, context) { palette ->
                if (palette.darkMutedSwatch?.rgb != null)
                    background = Color(palette.darkMutedSwatch?.rgb!!)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        if (image != null)
            Image(
                bitmap = image!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

        MovieDescription(
            movie = movie,
            navHostController = navHostController,
            background = background,
            modifier = Modifier.align(BottomStart)
        )
    }
    MovieActionIconsColumn(movie, Modifier.align(Alignment.TopEnd))

}

private fun calculateMovieRuntime(runtime: Int): String {
    val hours = runtime.div(60)
    val minutes = runtime.mod(60)
    return "$hours:$minutes"
}


@OptIn(ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
private fun MovieDescription(
    movie: MovieDetailsResponse,
    navHostController: NavHostController,
    background: Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier.animateContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(16.dp)
        ) {
            Text(text = "Rating %.1f".format(movie.voteAverage), fontWeight = FontWeight.Bold)
            Text(text = "Release Date ${movie.releaseDate}", fontWeight = FontWeight.Bold)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(16.dp)
        ) {
            Text(
                text = "Runtime ${calculateMovieRuntime(movie.runtime ?: 0)}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Popularity %.1f".format(movie.popularity), fontWeight = FontWeight.Bold)
        }


        MovieCardDescription(background, movie, navHostController)
    }

}

@Composable
private fun MovieCardDescription(
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
                        modifier = Modifier.align(CenterHorizontally)
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
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                    }
                }

        }
    }
}

@Composable
private fun SimilarMoviesButton(
    navHostController: NavHostController,
    movie: MovieDetailsResponse
) {
    OutlinedButton(
        onClick = {
            navHostController.navigate(
                "${Screens.SIMILAR_MOVIES_SCREEN}/${movie.id ?: 1}/${
                    movie.posterPath?.substring(
                        1,
                        movie.posterPath?.length ?: 1
                    )
                }"
            )
        }
    ) {
        Text(text = "see similar movies")
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun CastList(it: List<Cast>, navHostController: NavHostController) {
    if (it.isEmpty()) return
    Text(text = "Cast", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    LazyRow {
        items(it) { cast ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        val id = cast.id ?: 0
                        val params: HashMap<String, String> = HashMap(
                            mapOf(
                                "with_people" to id.toString()
                            )
                        )
                        val paramsString = Json.encodeToString(params)
                        navHostController.navigate("${Screens.DISCOVER_SCREEN}/$paramsString")
                    },
                horizontalAlignment = CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = Url.getImageUrl(
                            cast.profilePath ?: ""
                        ),
                        builder = {
                            transformations(CircleCropTransformation())
                            placeholder(R.drawable.icon_loading)
                            crossfade(true)
                        },
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.Black, CircleShape)
                )
                Text(text = cast.name ?: "")
            }
        }
    }
}

@Composable
private fun GenreChips(
    movie: MovieDetailsResponse,
    onClick: (Int) -> Unit
) {
    val genres = movie.genres?.mapNotNull { it.name } ?: emptyList()
    if (genres.isNotEmpty())
        ChipsListRow(
            values = genres,
            label = "Genres"
        ) {
            onClick(it)
        }
}

@Composable
private fun KeywordsChips(
    movie: MovieDetailsResponse,
    onClick: (Int) -> Unit
) {
    val keywords = movie.keywords?.keywords?.mapNotNull { it.name } ?: emptyList()
    if (keywords.isNotEmpty())
        ChipsListRow(
            values = keywords,
            label = "Keywords"
        ) {
            onClick(it)
        }
}

@Composable
private fun ChipsListRow(
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
fun ChipsListRow(values: List<String>, onClick: (Int) -> Unit) {
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

@Composable
private fun MovieActionIconsColumn(movie: MovieDetailsResponse, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        val viewModel: MovieViewModel = getViewModel()
        var favorite by remember {
            mutableStateOf(movie.accountStatesResponse?.favorite ?: false)
        }

        val rated = movie.accountStatesResponse?.rated?.isRated ?: false
        IconButton(onClick = {
            viewModel.markAsFavorite()
            favorite = favorite.not()
        }) {
            Icon(
                imageVector = if (favorite)
                    Icons.Filled.Favorite
                else
                    Icons.Outlined.Favorite, contentDescription = null,
                tint = if (favorite)
                    Color.Red
                else
                    Color.White
            )
        }
        CreateVerticalSpacer()
        RateAction(rated, movie.accountStatesResponse?.rated?.value)
        CreateVerticalSpacer()
        AddToWatchlistListAction()
    }
}

@Composable
fun AddToWatchlistListAction() {
    val viewModel: MovieViewModel = getViewModel()
    val movie by viewModel.movie
    var watchList by remember {
        mutableStateOf(movie.data?.accountStatesResponse?.watchlist ?: false)
    }
    IconButton(onClick = {
        viewModel.addToList()
        watchList = watchList.not()
    }) {
        Icon(
            imageVector = Icons.Default.WatchLater,
            contentDescription = null,
            tint = if (watchList) Color.Yellow else Color.White
        )
    }
}

@Composable
private fun RateAction(rated: Boolean, value: Float? = null) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val viewModel: MovieViewModel = getViewModel()
    if (showDialog)
        RateMotionLayout(
            initialProgress = value ?: 0f,
            onDismissRequest = {
                showDialog = false
            },
            onRemoveRate = {
                viewModel.removeRate()
            },
            onRate = {
                viewModel.rateMovie(it)
            }
        )
    IconButton(onClick = {
        showDialog = true
    }) {
        Icon(
            imageVector = Icons.Filled.StarRate,
            contentDescription = null,
            tint = if (rated)
                Color.Yellow
            else
                Color.White
        )
    }
}


@Composable
fun CreateVerticalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.height(dp))
}
