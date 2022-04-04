package com.example.movieappapi.presentation.screen.home

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
import com.example.movieappapi.presentation.components.ResourceErrorSnackBar
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
    val upcomingMovies by viewModel.upcomingMovies

    BoxWithConstraints {
        val height = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 64.dp)
        ) {
            PopularMovieViewPager(
                movies = popularMovies.data?.results ?: emptyList(),
                navHostController = navHostController,
                imageHeight = height.div(2.5f)
            )
            CreateVerticalSpacer()
            MovieHorizontalList(nowPlayingMovies, navHostController, "Now Playing") {
                viewModel.setNowPlayingMovies()
            }
            CreateVerticalSpacer(dp = 4.dp)
            MovieHorizontalList(upcomingMovies, navHostController, "Upcoming") {
                viewModel.setNowPlayingMovies()
            }
            CreateVerticalSpacer(4.dp)
            MovieHorizontalList(topRatedMovies, navHostController, "Top Rated") {
                viewModel.setTopRatedMovies()
            }

        }
        ResourceErrorSnackBar(resource = popularMovies, actionText = "Retry") {}
        ResourceErrorSnackBar(resource = nowPlayingMovies, actionText = "Retry") {}
        ResourceErrorSnackBar(resource = upcomingMovies, actionText = "Retry") {}
        ResourceErrorSnackBar(resource = topRatedMovies, actionText = "Retry") {}
    }
}


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun MovieHorizontalList(
    moviesResponse: Resource<MoviesResponse>,
    navHostController: NavHostController,
    label: String,
    paginate: () -> Unit
) {

    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
    CreateVerticalSpacer(dp = 4.dp)
    moviesResponse.OnSuccessComposable {
        it.results?.let { movies ->
            HorizontalMovieList(movies = movies, navHostController = navHostController) {
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