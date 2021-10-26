package com.example.movieappapi.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieappapi.dataModels.Movie
import com.example.movieappapi.utils.Screens
import com.example.movieappapi.utils.Url

@ExperimentalCoilApi
@Composable
fun MovieDetails(movie: Movie, navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Log.d("movie", "MovieDetails: ${Url.getImageUrl(movie.posterPath ?: "")}")
        Image(
            painter = rememberImagePainter(data = Url.getImageUrl(movie.posterPath ?: "")),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentScale = ContentScale.FillBounds
        )
        MovieDescription(
            movie = movie,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.BottomStart),
            navHostController = navHostController
        )
    }
}

@Composable
private fun MovieDescription(
    movie: Movie,
    modifier: Modifier,
    navHostController: NavHostController
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title ?: "",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.StarRate, contentDescription = null)
                    Text(text = "${movie.voteAverage}")
                }
            }
            CreateVerticalSpacer(4.dp)
            Text(text = "action, drama, thrill")
            CreateVerticalSpacer(dp = 4.dp)
            Text(text = movie.overview ?: "")
            CreateVerticalSpacer(4.dp)
            OutlinedButton(
                onClick = {
                    navHostController.navigate("${Screens.SIMILAR_MOVIES_SCREEN}/${movie.id}/${movie.title}")
                }
            ) {
                Text(text = "see  similar movies")
            }
        }
    }
}

@Composable
fun CreateVerticalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.height(dp))
}
