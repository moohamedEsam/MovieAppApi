package com.example.movieappapi.presentation.screen.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.movieappapi.presentation.components.HorizontalMovieList
import com.example.movieappapi.presentation.screen.movie.CreateVerticalSpacer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
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
                moviesResponse.results?.let {
                    Log.i("mainFeedScreen", "MainFeed: ${it.size}")
                    PopularMovieViewPager(
                        movies = it,
                        navHostController = navHostController,
                        imageHeight = height.div(2.5f)
                    )
                }
            }
            CreateVerticalSpacer(dp = 4.dp)
            TopRatedMovieList(topRatedMovies, navHostController) {
                viewModel.setTopRatedMovies()
            }
            CreateVerticalSpacer()
            NowPlayingMoviesList(nowPlayingMovies, navHostController) {
                viewModel.setNowPlayingMovies()
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun NowPlayingMoviesList(
    nowPlayingMovies: Resource<MoviesResponse>,
    navHostController: NavHostController,
    paginate: () -> Unit
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
            HorizontalMovieList(movies = it, navHostController = navHostController) {
                paginate()
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun TopRatedMovieList(
    topRatedMovies: Resource<MoviesResponse>,
    navHostController: NavHostController,
    paginate: () -> Unit
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
            HorizontalMovieList(movies = it, navHostController = navHostController) {
                paginate()
            }
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
                navigateToMovieDetail(movie.id ?: 0, navHostController)
            },
        contentScale = ContentScale.FillBounds
    )
}


fun navigateToMovieDetail(
    movieId: Int,
    navHostController: NavHostController
) {
    navHostController.navigate("${Screens.MOVIE_DETAILS}/$movieId")
}

