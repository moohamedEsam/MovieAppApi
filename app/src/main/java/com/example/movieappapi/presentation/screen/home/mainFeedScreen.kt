package com.example.movieappapi.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.domain.model.Movie
import com.example.movieappapi.domain.model.MoviesResponse
import com.example.movieappapi.domain.utils.Resource
import com.example.movieappapi.domain.utils.Screens
import com.example.movieappapi.domain.utils.Url
import com.example.movieappapi.presentation.screen.home.MainFeedViewModel
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainFeed(navHostController: NavHostController) {
    val viewModel: MainFeedViewModel = getViewModel()
    val popularMovies by viewModel.popularMovies
    val topRatedMovies by viewModel.topRatedMovies
    val nowPlayingMovies by viewModel.nowPlayingMovies
    BoxWithConstraints {
        val height = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            popularMovies.HandleResourceChange { moviesResponse ->
                Log.d("mainFeedScreen", "MainFeed: called")
                moviesResponse.results?.let {
                    PopularMovieViewPager(
                        movies = it,
                        navHostController = navHostController,
                        imageHeight = height.div(2.5f)
                    )
                }
            }
            CreateVerticalSpacer(dp = 4.dp)
            TopRatedMovieList(topRatedMovies, navHostController)
            CreateVerticalSpacer()
            NowPlayingMoviesList(nowPlayingMovies, navHostController)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun NowPlayingMoviesList(
    nowPlayingMovies: Resource<MoviesResponse>,
    navHostController: NavHostController
) {
    Text(
        text = "now playing",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
    CreateVerticalSpacer(4.dp)
    nowPlayingMovies.HandleResourceChange { moviesResponse ->
        moviesResponse.results?.let {
            HorizontalMovieList(movies = it, navHostController = navHostController)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun TopRatedMovieList(
    topRatedMovies: Resource<MoviesResponse>,
    navHostController: NavHostController
) {
    Text(
        text = "top rated",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
    CreateVerticalSpacer(dp = 4.dp)
    topRatedMovies.HandleResourceChange { moviesResponse ->
        moviesResponse.results?.let {
            HorizontalMovieList(movies = it, navHostController = navHostController)
        }
    }
}

@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun PopularMovieViewPager(
    movies: List<Movie>,
    navHostController: NavHostController,
    imageHeight: Dp
) {
    val state = rememberPagerState(1)
    LaunchedEffect(key1 = state.currentPage) {
        delay(3000)
        if (state.currentPage == movies.lastIndex)
            state.animateScrollToPage(1)
        else
            state.animateScrollToPage(state.currentPage + 1)

    }
    HorizontalPager(
        count = movies.size,
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight),
        state = state
    ) { page ->
        PagerMovieItem(movie = movies[page], navHostController = navHostController)
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HorizontalMovieList(movies: List<Movie>, navHostController: NavHostController) {
    LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(movies) {
            HorizontalListMovieItem(movie = it, navHostController = navHostController)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HorizontalListMovieItem(movie: Movie, navHostController: NavHostController) {
    val isVisible = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = isVisible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .padding(horizontal = 4.dp)
        ) {
            Card(modifier = Modifier
                .size(90.dp, 120.dp)
                .clickable {
                    navigateToMovieDetail(movie = movie, navHostController = navHostController)
                }
            ) {
                Image(
                    painter = rememberImagePainter(data = Url.getImageUrl(movie.posterPath ?: "")),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            CreateVerticalSpacer(2.dp)
            Text(text = movie.title ?: "", maxLines = 2, fontWeight = FontWeight.Bold)
        }
    }
}

@ExperimentalSerializationApi
@ExperimentalCoilApi
@Composable
fun PagerMovieItem(movie: Movie, navHostController: NavHostController) {
    Image(
        painter = rememberImagePainter(data = Url.getImageUrl(movie.posterPath ?: "")),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navigateToMovieDetail(movie, navHostController)
            },
        contentScale = ContentScale.FillBounds
    )
}


fun navigateToMovieDetail(
    movie: Movie,
    navHostController: NavHostController
) {
    val json = Json { isLenient = true }
    val movieString = json
        .encodeToString(movie)
        .replace("/", "*")
    navHostController.navigate("${Screens.MOVIE_DETAILS}/$movieString")
}

