package com.example.movieappapi.presentation.screen.movie

import android.graphics.Bitmap
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
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
import com.example.movieappapi.presentation.components.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun MovieDetails(movieId: Int, navHostController: NavHostController) {
    val viewModel: MovieViewModel = getViewModel()
    val movie by viewModel.movie
    val rateResponse by viewModel.rateResponse
    val favoriteResponse by viewModel.favoriteResponse
    val watchlistResponse by viewModel.watchlistResponse
    LaunchedEffect(key1 = Unit) {
        viewModel.setMovie(movieId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        movie.OnSuccessComposable {
            MovieUi(movie = it, navHostController = navHostController)
        }
        ResourceErrorSnackBar(resource = rateResponse, "") {}
        ResourceErrorSnackBar(resource = favoriteResponse, "") {}
        ResourceErrorSnackBar(resource = watchlistResponse, "") {}

    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoilApi::class)
@Composable
fun BoxScope.MovieUi(
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

fun calculateMovieRuntime(runtime: Int): String {
    val hours = runtime.div(60)
    val minutes = runtime.mod(60)
    return "$hours:$minutes"
}


@OptIn(ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
fun MovieDescription(
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
                text = "Duration ${calculateMovieRuntime(movie.runtime ?: 0)}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Popularity %.1f".format(movie.popularity), fontWeight = FontWeight.Bold)
        }


        MovieCardDescription(background, movie, navHostController)
    }

}

@Composable
fun SimilarMoviesButton(
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
fun CastList(it: List<Cast>, navHostController: NavHostController) {
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
fun GenreChips(
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
fun KeywordsChips(
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
fun CreateVerticalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.height(dp))
}
