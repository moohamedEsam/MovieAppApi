package com.example.movieappapi.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.dataModels.Movie
import com.example.movieappapi.utils.Screens
import com.example.movieappapi.utils.Url
import com.example.movieappapi.viewModels.MainFeedViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel

@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainFeed(navHostController: NavHostController) {
    val viewModel: MainFeedViewModel = getViewModel()
    val popularMovies by viewModel.popularMovies
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        popularMovies.DisplayComposable {
            Log.d("mainFeedScreen", "MainFeed: called")
            popularMovies.data?.results?.let {
                PopularMovieViewPager(movies = it, navHostController = navHostController)
            }
        }
    }
}

@ExperimentalSerializationApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun PopularMovieViewPager(movies: List<Movie>, navHostController: NavHostController) {
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
            .fillMaxHeight(0.4f),
        state = state
    ) { page ->
        PagerMovieItem(movie = movies[page], navHostController = navHostController)
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
                val json = Json { isLenient = true }
                val movieString = json
                    .encodeToString(movie)
                    .replace("/", "*")
                navHostController.navigate("${Screens.MOVIE_DETAILS}/$movieString")
            },
        contentScale = ContentScale.FillBounds
    )
}