package com.example.movieappapi.presentation.screen.movie

import android.graphics.Bitmap
import android.util.Log
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
import com.example.movieappapi.presentation.components.RateMotionLayout
import com.example.movieappapi.presentation.components.UserListsDropDownMenu
import com.example.movieappapi.presentation.components.getPalette
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


@OptIn(ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
private fun MovieDescription(
    movie: MovieDetailsResponse,
    navHostController: NavHostController,
    background: Color,
    modifier: Modifier
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
        modifier = modifier.animateContentSize()

    ) {
        Card(
            backgroundColor = background,
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            modifier = Modifier
                .alpha(0.9f)
                .draggable(
                    state = rememberDraggableState {
                        Log.i("movieScreen", "MovieDescription: $it")
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
                        .draggable(
                            state = rememberDraggableState {
                                cardVisible = it < 0
                            },
                            orientation = Orientation.Vertical
                        )
                ) {
                    IconButton(
                        onClick = {
                            cardVisible = false
                        },
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        Icon(imageVector = Icons.Default.ExpandLess, contentDescription = null)
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
                            val keywordString = Json.encodeToString(keyword)
                            navHostController.navigate("${Screens.KEYWORD_SCREEN}/$keywordString")
                        }
                    }
                    CreateVerticalSpacer(4.dp)
                    GenreChips(movie) {}
                    CreateVerticalSpacer(dp = 4.dp)
                    movie.movieCredits?.cast?.let {
                        CastList(it)
                    }
                    CreateVerticalSpacer(4.dp)
                    Text(text = movie.overview ?: "")
                    CreateVerticalSpacer(4.dp)
                    SimilarMoviesButton(navHostController, movie)
                }
            else
                Column(modifier = Modifier.fillMaxWidth()) {
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
private fun CastList(it: List<Cast>) {
    if (it.isEmpty()) return
    Text(text = "Cast", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    LazyRow {
        items(it) { cast ->
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = Url.getImageUrl(
                            cast.profilePath ?: ""
                        ),
                        builder = {
                            transformations(CircleCropTransformation())
                            placeholder(R.drawable.ic_launcher_foreground)
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
        RateAction(rated)
        CreateVerticalSpacer()
        AddToListAction()
    }
}

@Composable
fun AddToListAction() {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val viewModel: MovieViewModel = getViewModel()
    IconButton(onClick = {
        showDialog = true
    }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
    UserListsDropDownMenu(
        expanded = showDialog,
        onDismissRequest = { showDialog = false }
    ) {
        viewModel.addToList(it)
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

//@Preview
//@Composable
//fun MoviePreview() {
//    val movie: MovieDetailsResponse = Json.decodeFromString(
//        "{\"adult\":false,\"backdrop_path\":\"/2hGjmgZrS1nlsEl5PZorn7EsmzH.jpg\",\"belongs_to_collection\":null,\"budget\":43000000,\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":53,\"name\":\"Thriller\"}],\"homepage\":\"https://www.blacklightmov.com/\",\"id\":823625,\"imdb_id\":\"tt14060094\",\"original_language\":\"en\",\"original_title\":\"Blacklight\",\"overview\":\"Travis Block is a shadowy Government agent who specializes in removing operatives whose covers have been exposed. He then has to uncover a deadly conspiracy within his own ranks that reaches the highest echelons of power.\",\"popularity\":2289.078,\"poster_path\":\"/7gFo1PEbe1CoSgNTnjCGdZbw0zP.jpg\",\"production_companies\":[{\"id\":33768,\"logo_path\":\"/f8TDXTNoj4G4eRCKlUpG3KYQj9I.png\",\"name\":\"The Solution\",\"origin_country\":\"US\"},{\"id\":5357,\"logo_path\":\"/19A0Ilxeh1bWMlyMtMgGzcNBn07.png\",\"name\":\"Zero Gravity Management\",\"origin_country\":\"US\"},{\"id\":11840,\"logo_path\":\"/8oyCRYf6ezxB5h8HJIPVG8JjzgL.png\",\"name\":\"Film Victoria\",\"origin_country\":\"AU\"},{\"id\":69756,\"logo_path\":null,\"name\":\"Lightstream Entertainment\",\"origin_country\":\"AU\"},{\"id\":7584,\"logo_path\":\"/eGkfvvyf4fJTvBUR1xt1669IPA3.png\",\"name\":\"Screen Australia\",\"origin_country\":\"AU\"}],\"production_countries\":[{\"iso_3166_1\":\"AU\",\"name\":\"Australia\"},{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"2022-02-10\",\"revenue\":10000000,\"runtime\":104,\"spoken_languages\":[{\"english_name\":\"English\",\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"They're gonna need more men.\",\"title\":\"Blacklight\",\"video\":false,\"vote_average\":5.9,\"vote_count\":216,\"account_states\":{\"favorite\":true,\"rated\":false,\"watchlist\":false},\"keywords\":{\"keywords\":[]},\"credits\":{\"cast\":[{\"adult\":false,\"gender\":2,\"id\":3896,\"known_for_department\":\"Acting\",\"name\":\"Liam Neeson\",\"original_name\":\"Liam Neeson\",\"popularity\":30.711,\"profile_path\":\"/bboldwqSC6tdw2iL6631c98l2Mn.jpg\",\"cast_id\":2,\"character\":\"Travis Block\",\"credit_id\":\"60d5c9239979d20073ed12e8\",\"order\":0},{\"adult\":false,\"gender\":1,\"id\":2026050,\"known_for_department\":\"Acting\",\"name\":\"Emmy Raver-Lampman\",\"original_name\":\"Emmy Raver-Lampman\",\"popularity\":7.707,\"profile_path\":\"/cBkHUBzqoqrnkxDXWlqQmm91pD2.jpg\",\"cast_id\":25,\"character\":\"Mira Jones\",\"credit_id\":\"61d4c4c831644b00663067f0\",\"order\":1},{\"adult\":false,\"gender\":2,\"id\":1369329,\"known_for_department\":\"Acting\",\"name\":\"Taylor John Smith\",\"original_name\":\"Taylor John Smith\",\"popularity\":20.139,\"profile_path\":\"/6dXZRf8ePa1oxQN9a5hFGEjDoI0.jpg\",\"cast_id\":26,\"character\":\"Dusty Crane\",\"credit_id\":\"61d4c4fa9defda008dfe8c3c\",\"order\":2},{\"adult\":false,\"gender\":2,\"id\":18992,\"known_for_department\":\"Acting\",\"name\":\"Aidan Quinn\",\"original_name\":\"Aidan Quinn\",\"popularity\":26.601,\"profile_path\":\"/oNanwnXr6OApFUa4vaswd6RYVYB.jpg\",\"cast_id\":27,\"character\":\"Gabriel Robinson\",\"credit_id\":\"61d4c531e194b0008cf6c176\",\"order\":3},{\"adult\":false,\"gender\":2,\"id\":123724,\"known_for_department\":\"Acting\",\"name\":\"Tim Draxl\",\"original_name\":\"Tim Draxl\",\"popularity\":2.118,\"profile_path\":\"/4WeKn0jlTHwmMnlhjd2EsqE4uFp.jpg\",\"cast_id\":16,\"character\":\"Drew Hawthorne\",\"credit_id\":\"60d5ca649979d2005d566cf1\",\"order\":4},{\"adult\":false,\"gender\":1,\"id\":114843,\"known_for_department\":\"Acting\",\"name\":\"Claire van der Boom\",\"original_name\":\"Claire van der Boom\",\"popularity\":14.783,\"profile_path\":\"/1CXZLmtimJksArEZ5afDIH6prwW.jpg\",\"cast_id\":28,\"character\":\"Amanda Block\",\"credit_id\":\"61d4c7a01b722c5dbb3c86b4\",\"order\":5},{\"adult\":false,\"gender\":0,\"id\":1635549,\"known_for_department\":\"Crew\",\"name\":\"Michael M. Foster\",\"original_name\":\"Michael M. Foster\",\"popularity\":1.778,\"profile_path\":\"/cds8Fc9NEvQm4oBp2vg0MWqMh2B.jpg\",\"cast_id\":17,\"character\":\"Police Officer\",\"credit_id\":\"60d5ca723a9937002861c82f\",\"order\":6},{\"adult\":false,\"gender\":1,\"id\":1325837,\"known_for_department\":\"Acting\",\"name\":\"Yesse Spence\",\"original_name\":\"Yesse Spence\",\"popularity\":6.863,\"profile_path\":\"/vRUM9gtTlFCzI97YISnzq6X4fC4.jpg\",\"cast_id\":18,\"character\":\"Principal\",\"credit_id\":\"60d5ca79a6d931004652552e\",\"order\":7},{\"adult\":false,\"gender\":2,\"id\":207679,\"known_for_department\":\"Acting\",\"name\":\"Todd Levi\",\"original_name\":\"Todd Levi\",\"popularity\":1.4,\"profile_path\":\"/e2WLbk5qFZ7RGhAKOA5z4ZbXbg8.jpg\",\"cast_id\":20,\"character\":\"Lecturer\",\"credit_id\":\"60d5ca8aef9d720074de0ad3\",\"order\":8},{\"adult\":false,\"gender\":1,\"id\":3438486,\"known_for_department\":\"Acting\",\"name\":\"Gabriella Sengos\",\"original_name\":\"Gabriella Sengos\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":29,\"character\":\"Natalie Block\",\"credit_id\":\"6214bae1d3d387006b2f129f\",\"order\":9},{\"adult\":false,\"gender\":0,\"id\":3310690,\"known_for_department\":\"Acting\",\"name\":\"Clara Helms\",\"original_name\":\"Clara Helms\",\"popularity\":0.828,\"profile_path\":null,\"cast_id\":65,\"character\":\"Blaire Wright\",\"credit_id\":\"6225ccde07a808006adc798c\",\"order\":10},{\"adult\":false,\"gender\":1,\"id\":1028560,\"known_for_department\":\"Acting\",\"name\":\"Yael Stone\",\"original_name\":\"Yael Stone\",\"popularity\":16.11,\"profile_path\":\"/kwTxf4hCMKqblOS2xuwYf9BNW4i.jpg\",\"cast_id\":66,\"character\":\"Helen Davidson\",\"credit_id\":\"6225cd55a7e363006c4b08b7\",\"order\":11},{\"adult\":false,\"gender\":2,\"id\":2628361,\"known_for_department\":\"Acting\",\"name\":\"Andrew Shaw\",\"original_name\":\"Andrew Shaw\",\"popularity\":0.6,\"profile_path\":\"/zlV68c5KwZHzzWv6aMY0X0YYKTz.jpg\",\"cast_id\":67,\"character\":\"Jordan Lockhart\",\"credit_id\":\"6225cd6da7e363001c5552a1\",\"order\":12},{\"adult\":false,\"gender\":2,\"id\":2981088,\"known_for_department\":\"Acting\",\"name\":\"Zac Lemons\",\"original_name\":\"Zac Lemons\",\"popularity\":0.98,\"profile_path\":null,\"cast_id\":68,\"character\":\"Wallace\",\"credit_id\":\"6225cd89a3770a006f1c9fe2\",\"order\":13},{\"adult\":false,\"gender\":1,\"id\":1371799,\"known_for_department\":\"Acting\",\"name\":\"Georgia Flood\",\"original_name\":\"Georgia Flood\",\"popularity\":8.267,\"profile_path\":\"/88nCsL1jHGeJn6qGZ6IRXWODbRc.jpg\",\"cast_id\":69,\"character\":\"Pearl\",\"credit_id\":\"6225cdbcb339030066277cf8\",\"order\":14},{\"adult\":false,\"gender\":1,\"id\":216802,\"known_for_department\":\"Acting\",\"name\":\"Caroline Brazier\",\"original_name\":\"Caroline Brazier\",\"popularity\":9.786,\"profile_path\":\"/iW87NQ1x6c8d992Qz5S2JSQJSVi.jpg\",\"cast_id\":70,\"character\":\"Sarah\",\"credit_id\":\"6225cdd2a3770a001cc1fe34\",\"order\":15},{\"adult\":false,\"gender\":1,\"id\":2637828,\"known_for_department\":\"Acting\",\"name\":\"Melanie Jarnson\",\"original_name\":\"Melanie Jarnson\",\"popularity\":15.246,\"profile_path\":\"/9t0wxqberiCWOepbVVPcsBgYChA.jpg\",\"cast_id\":71,\"character\":\"Sofia Elores\",\"credit_id\":\"6225cde7c8113d0044b27a88\",\"order\":16},{\"adult\":false,\"gender\":2,\"id\":2391549,\"known_for_department\":\"Acting\",\"name\":\"Sunny S. Walia\",\"original_name\":\"Sunny S. Walia\",\"popularity\":0.6,\"profile_path\":\"/ju7GWd7GrOUUYmbWWtpXKqqLcRd.jpg\",\"cast_id\":72,\"character\":\"Beat Cop\",\"credit_id\":\"6225cdfb07a8080043c354ff\",\"order\":17},{\"adult\":false,\"gender\":2,\"id\":3040149,\"known_for_department\":\"Acting\",\"name\":\"Linc Hasler\",\"original_name\":\"Linc Hasler\",\"popularity\":0.694,\"profile_path\":null,\"cast_id\":73,\"character\":\"Bald Guy\",\"credit_id\":\"6225ce3fedeb430046f8a3e9\",\"order\":18},{\"adult\":false,\"gender\":2,\"id\":2272447,\"known_for_department\":\"Acting\",\"name\":\"Anthony J. Sharpe\",\"original_name\":\"Anthony J. Sharpe\",\"popularity\":6.009,\"profile_path\":\"/txNw1q9fn9S0Olib1yWTJu1SayR.jpg\",\"cast_id\":74,\"character\":\"Nationalist Leader\",\"credit_id\":\"6225ce4e31d09b006e08df77\",\"order\":19},{\"adult\":false,\"gender\":2,\"id\":3045451,\"known_for_department\":\"Acting\",\"name\":\"Cam Faull\",\"original_name\":\"Cam Faull\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":75,\"character\":\"Bearded Nationalist #2\",\"credit_id\":\"6225ce5cc9044c001c119192\",\"order\":20},{\"adult\":false,\"gender\":2,\"id\":187016,\"known_for_department\":\"Acting\",\"name\":\"Jasper Bagg\",\"original_name\":\"Jasper Bagg\",\"popularity\":2.397,\"profile_path\":\"/fJTpfS6yK78XbO8LWXnXK1Supdm.jpg\",\"cast_id\":76,\"character\":\"Bearded Nationalist #3\",\"credit_id\":\"6225ce6b7c6de3001bd4c35c\",\"order\":21},{\"adult\":false,\"gender\":2,\"id\":164583,\"known_for_department\":\"Acting\",\"name\":\"Joe Petruzzi\",\"original_name\":\"Joe Petruzzi\",\"popularity\":1.188,\"profile_path\":\"/iBrqTqmrOwyOsfnVPQ1FmWuIZWh.jpg\",\"cast_id\":77,\"character\":\"Desk Captain\",\"credit_id\":\"6225ce7807a8080043c3558d\",\"order\":22},{\"adult\":false,\"gender\":2,\"id\":2371816,\"known_for_department\":\"Acting\",\"name\":\"Alex Cooke\",\"original_name\":\"Alex Cooke\",\"popularity\":0.98,\"profile_path\":\"/wpIdUbpsLx88EuaioQpAzXATnVx.jpg\",\"cast_id\":78,\"character\":\"Deputy 1\",\"credit_id\":\"6225ce996f53e1006f473c40\",\"order\":23},{\"adult\":false,\"gender\":0,\"id\":1215130,\"known_for_department\":\"Acting\",\"name\":\"Damien Bodie\",\"original_name\":\"Damien Bodie\",\"popularity\":2.219,\"profile_path\":null,\"cast_id\":79,\"character\":\"Deputy 2\",\"credit_id\":\"6225cea76f53e10018d2277d\",\"order\":24},{\"adult\":false,\"gender\":0,\"id\":3456138,\"known_for_department\":\"Acting\",\"name\":\"Andriana Williams\",\"original_name\":\"Andriana Williams\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":80,\"character\":\"Margaret Robinson / 911 Operator\",\"credit_id\":\"6225ceb6c8113d006ddcfeea\",\"order\":25},{\"adult\":false,\"gender\":1,\"id\":2516798,\"known_for_department\":\"Acting\",\"name\":\"Anita Torrance\",\"original_name\":\"Anita Torrance\",\"popularity\":0.652,\"profile_path\":null,\"cast_id\":81,\"character\":\"Secretary\",\"credit_id\":\"6225cec86f53e1006f473c8d\",\"order\":26},{\"adult\":false,\"gender\":0,\"id\":1349382,\"known_for_department\":\"Acting\",\"name\":\"Irene Chen\",\"original_name\":\"Irene Chen\",\"popularity\":0.6,\"profile_path\":\"/f5WHOz2VfQvhLI2iNLanhrQHxSV.jpg\",\"cast_id\":82,\"character\":\"Waitress\",\"credit_id\":\"6225cedfbccf1e0072e4ea02\",\"order\":27},{\"adult\":false,\"gender\":1,\"id\":3456139,\"known_for_department\":\"Acting\",\"name\":\"Dailin Gabrielle\",\"original_name\":\"Dailin Gabrielle\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":83,\"character\":\"Young Female Reporter\",\"credit_id\":\"6225cef5bccf1e0072e4ea4b\",\"order\":28},{\"adult\":false,\"gender\":2,\"id\":3456141,\"known_for_department\":\"Acting\",\"name\":\"Rodney Miller\",\"original_name\":\"Rodney Miller\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":84,\"character\":\"Unconscious Jetta Driver\",\"credit_id\":\"6225cf078e2ba6001c46692e\",\"order\":29},{\"adult\":false,\"gender\":1,\"id\":3136102,\"known_for_department\":\"Acting\",\"name\":\"Amelia Forsyth-Smith\",\"original_name\":\"Amelia Forsyth-Smith\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":19,\"character\":\"\",\"credit_id\":\"60d5ca803a993700801d37f9\",\"order\":30},{\"adult\":false,\"gender\":1,\"id\":3136103,\"known_for_department\":\"Acting\",\"name\":\"Yanina Clifton\",\"original_name\":\"Yanina Clifton\",\"popularity\":0.6,\"profile_path\":null,\"cast_id\":22,\"character\":\"\",\"credit_id\":\"60d5caa1a6d931002c89a502\",\"order\":32}],\"crew\":[{\"adult\":false,\"gender\":1,\"id\":5914,\"known_for_department\":\"Production\",\"name\":\"Mary Vernieu\",\"original_name\":\"Mary Vernieu\",\"popularity\":6.285,\"profile_path\":null,\"credit_id\":\"6225c71d2e2b2c0045a585e5\",\"department\":\"Production\",\"job\":\"Casting\"},{\"adult\":false,\"gender\":2,\"id\":4140,\"known_for_department\":\"Sound\",\"name\":\"Mark Isham\",\"original_name\":\"Mark Isham\",\"popularity\":2.778,\"profile_path\":\"/ayQ1j1T3wFk44kliy1u4atTT8um.jpg\",\"credit_id\":\"60d5ca2e25b955005df7ef39\",\"department\":\"Sound\",\"job\":\"Original Music Composer\"},{\"adult\":false,\"gender\":2,\"id\":4950,\"known_for_department\":\"Camera\",\"name\":\"Shelly Johnson\",\"original_name\":\"Shelly Johnson\",\"popularity\":1.267,\"profile_path\":\"/aueV77RifcZBEQT8rZfEibAQ9At.jpg\",\"credit_id\":\"60d5ca397aecc6005e82d4f4\",\"department\":\"Camera\",\"job\":\"Director of Photography\"},{\"adult\":false,\"gender\":1,\"id\":9345,\"known_for_department\":\"Art\",\"name\":\"Michelle McGahey\",\"original_name\":\"Michelle McGahey\",\"popularity\":2.252,\"profile_path\":null,\"credit_id\":\"60d5ca4def9d72002fad0db8\",\"department\":\"Art\",\"job\":\"Production Design\"},{\"adult\":false,\"gender\":0,\"id\":43095,\"known_for_department\":\"Production\",\"name\":\"Tom Ortenberg\",\"original_name\":\"Tom Ortenberg\",\"popularity\":0.755,\"profile_path\":null,\"credit_id\":\"6225c7c007a808001b007fbe\",\"department\":\"Production\",\"job\":\"Executive Producer\"},{\"adult\":false,\"gender\":1,\"id\":75485,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Noriko Watanabe\",\"original_name\":\"Noriko Watanabe\",\"popularity\":1.24,\"profile_path\":null,\"credit_id\":\"6225c88531d09b006e08d6a5\",\"department\":\"Costume & Make-Up\",\"job\":\"Makeup Artist\"},{\"adult\":false,\"gender\":1,\"id\":75485,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Noriko Watanabe\",\"original_name\":\"Noriko Watanabe\",\"popularity\":1.24,\"profile_path\":null,\"credit_id\":\"6225c8934a4bfc0043f08e93\",\"department\":\"Costume & Make-Up\",\"job\":\"Hairstylist\"},{\"adult\":false,\"gender\":2,\"id\":87796,\"known_for_department\":\"Production\",\"name\":\"Craig McMahon\",\"original_name\":\"Craig McMahon\",\"popularity\":0.828,\"profile_path\":null,\"credit_id\":\"6225c7b3c8113d0044b2718f\",\"department\":\"Production\",\"job\":\"Executive Producer\"},{\"adult\":false,\"gender\":2,\"id\":166753,\"known_for_department\":\"Directing\",\"name\":\"Guy Norris\",\"original_name\":\"Guy Norris\",\"popularity\":1.055,\"profile_path\":null,\"credit_id\":\"60d5cb5466a7c3002dad829e\",\"department\":\"Crew\",\"job\":\"Stunts\"},{\"adult\":false,\"gender\":2,\"id\":166753,\"known_for_department\":\"Directing\",\"name\":\"Guy Norris\",\"original_name\":\"Guy Norris\",\"popularity\":1.055,\"profile_path\":null,\"credit_id\":\"6225cc8f8e2ba6001c4665a9\",\"department\":\"Directing\",\"job\":\"Second Unit Director\"},{\"adult\":false,\"gender\":2,\"id\":206574,\"known_for_department\":\"Production\",\"name\":\"Mark Williams\",\"original_name\":\"Mark Williams\",\"popularity\":6.144,\"profile_path\":\"/dmOeFb4qxG5TmDy1RW8CBzCCEOT.jpg\",\"credit_id\":\"60894b919a8a8a0041ffa7f0\",\"department\":\"Directing\",\"job\":\"Director\"},{\"adult\":false,\"gender\":2,\"id\":206574,\"known_for_department\":\"Production\",\"name\":\"Mark Williams\",\"original_name\":\"Mark Williams\",\"popularity\":6.144,\"profile_path\":\"/dmOeFb4qxG5TmDy1RW8CBzCCEOT.jpg\",\"credit_id\":\"60d5ca25ef9d7200460cdc9a\",\"department\":\"Production\",\"job\":\"Producer\"},{\"adult\":false,\"gender\":2,\"id\":206574,\"known_for_department\":\"Production\",\"name\":\"Mark Williams\",\"original_name\":\"Mark Williams\",\"popularity\":6.144,\"profile_path\":\"/dmOeFb4qxG5TmDy1RW8CBzCCEOT.jpg\",\"credit_id\":\"6225cfe4c995ee0043a9d77c\",\"department\":\"Writing\",\"job\":\"Writer\"},{\"adult\":false,\"gender\":2,\"id\":262023,\"known_for_department\":\"Acting\",\"name\":\"Jimmy Cummings\",\"original_name\":\"Jimmy Cummings\",\"popularity\":2.616,\"profile_path\":\"/fVedSxKuTiF4tJcC2qqkQ8M4jSf.jpg\",\"credit_id\":\"6225c7e7bccf1e0044ef2086\",\"department\":\"Production\",\"job\":\"Executive Producer\"},{\"adult\":false,\"gender\":2,\"id\":279590,\"known_for_department\":\"Production\",\"name\":\"David Newman\",\"original_name\":\"David Newman\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c7116f53e1006f47310a\",\"department\":\"Production\",\"job\":\"Casting\"},{\"adult\":false,\"gender\":0,\"id\":1014925,\"known_for_department\":\"Production\",\"name\":\"Myles Nestel\",\"original_name\":\"Myles Nestel\",\"popularity\":1.551,\"profile_path\":null,\"credit_id\":\"60d5ca1bef9d720074de0a31\",\"department\":\"Production\",\"job\":\"Producer\"},{\"adult\":false,\"gender\":0,\"id\":1024909,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Katherine Milne\",\"original_name\":\"Katherine Milne\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c6b68e2ba6001c465e23\",\"department\":\"Costume & Make-Up\",\"job\":\"Costume Design\"},{\"adult\":false,\"gender\":0,\"id\":1053661,\"known_for_department\":\"Production\",\"name\":\"Craig Chapman\",\"original_name\":\"Craig Chapman\",\"popularity\":0.894,\"profile_path\":null,\"credit_id\":\"6225c8034a4bfc006e3dbafd\",\"department\":\"Production\",\"job\":\"Executive Producer\"},{\"adult\":false,\"gender\":0,\"id\":1076632,\"known_for_department\":\"Writing\",\"name\":\"Aleve Mei Loh\",\"original_name\":\"Aleve Mei Loh\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c779c995ee006eb59a60\",\"department\":\"Production\",\"job\":\"Co-Producer\"},{\"adult\":false,\"gender\":0,\"id\":1087623,\"known_for_department\":\"Production\",\"name\":\"Tom McSweeney\",\"original_name\":\"Tom McSweeney\",\"popularity\":1.4,\"profile_path\":\"/igwW136ZaAjP4qWfUj3w1U0KWwy.jpg\",\"credit_id\":\"6225c70207a808006adc721e\",\"department\":\"Production\",\"job\":\"Casting\"},{\"adult\":false,\"gender\":0,\"id\":1141734,\"known_for_department\":\"Editing\",\"name\":\"Michael P. Shawver\",\"original_name\":\"Michael P. Shawver\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"60d5ca41aa789800451944bc\",\"department\":\"Editing\",\"job\":\"Editor\"},{\"adult\":false,\"gender\":0,\"id\":1319499,\"known_for_department\":\"Production\",\"name\":\"Paul Currie\",\"original_name\":\"Paul Currie\",\"popularity\":1.4,\"profile_path\":null,\"credit_id\":\"60d5ca12ef9d72002fad0d30\",\"department\":\"Production\",\"job\":\"Producer\"},{\"adult\":false,\"gender\":0,\"id\":1325186,\"known_for_department\":\"Art\",\"name\":\"Vanessa Cerne\",\"original_name\":\"Vanessa Cerne\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c6db95acf00064f05319\",\"department\":\"Art\",\"job\":\"Set Decoration\"},{\"adult\":false,\"gender\":0,\"id\":1380472,\"known_for_department\":\"Art\",\"name\":\"Jennifer A. Davis\",\"original_name\":\"Jennifer A. Davis\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c6ebb3390300662773b8\",\"department\":\"Art\",\"job\":\"Art Direction\"},{\"adult\":false,\"gender\":0,\"id\":1403712,\"known_for_department\":\"Production\",\"name\":\"Sandy Stevens\",\"original_name\":\"Sandy Stevens\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c8a0a7e36300452c1af3\",\"department\":\"Production\",\"job\":\"Unit Production Manager\"},{\"adult\":false,\"gender\":0,\"id\":1460588,\"known_for_department\":\"Crew\",\"name\":\"Hauk Olafsson\",\"original_name\":\"Hauk Olafsson\",\"popularity\":1.4,\"profile_path\":null,\"credit_id\":\"6225cc5f6f53e100433e55ec\",\"department\":\"Visual Effects\",\"job\":\"Senior Visual Effects Supervisor\"},{\"adult\":false,\"gender\":1,\"id\":1521110,\"known_for_department\":\"Production\",\"name\":\"Michelle Wade Byrd\",\"original_name\":\"Michelle Wade Byrd\",\"popularity\":1.96,\"profile_path\":null,\"credit_id\":\"6225c729a3770a0044d479e9\",\"department\":\"Production\",\"job\":\"Casting\"},{\"adult\":false,\"gender\":0,\"id\":1560423,\"known_for_department\":\"Visual Effects\",\"name\":\"Peter Stubbs\",\"original_name\":\"Peter Stubbs\",\"popularity\":1.536,\"profile_path\":null,\"credit_id\":\"6225cc44bccf1e001c419b19\",\"department\":\"Visual Effects\",\"job\":\"Special Effects Supervisor\"},{\"adult\":false,\"gender\":0,\"id\":1596905,\"known_for_department\":\"Directing\",\"name\":\"Matt Enfield\",\"original_name\":\"Matt Enfield\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225ccbd6f53e1006f473940\",\"department\":\"Directing\",\"job\":\"First Assistant Director\"},{\"adult\":false,\"gender\":1,\"id\":1634555,\"known_for_department\":\"Art\",\"name\":\"Belinda Cusmano\",\"original_name\":\"Belinda Cusmano\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c6f7a7e363006c4affec\",\"department\":\"Art\",\"job\":\"Supervising Art Director\"},{\"adult\":false,\"gender\":0,\"id\":1768068,\"known_for_department\":\"Production\",\"name\":\"Mario Biancacci\",\"original_name\":\"Mario Biancacci\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c819bccf1e0044ef20be\",\"department\":\"Production\",\"job\":\"Associate Producer\"},{\"adult\":false,\"gender\":2,\"id\":1775491,\"known_for_department\":\"Directing\",\"name\":\"Ted Green\",\"original_name\":\"Ted Green\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c8baa7e36300452c1b01\",\"department\":\"Directing\",\"job\":\"Script Supervisor\"},{\"adult\":false,\"gender\":0,\"id\":1811507,\"known_for_department\":\"Crew\",\"name\":\"John Sanderson\",\"original_name\":\"John Sanderson\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225cc5395acf0001c864c51\",\"department\":\"Visual Effects\",\"job\":\"Senior Visual Effects Supervisor\"},{\"adult\":false,\"gender\":0,\"id\":2076379,\"known_for_department\":\"Production\",\"name\":\"Jim Cardwell\",\"original_name\":\"Jim Cardwell\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c7417c6de300644e8cbf\",\"department\":\"Production\",\"job\":\"Co-Producer\"},{\"adult\":false,\"gender\":0,\"id\":2353095,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Emma Kingsbury\",\"original_name\":\"Emma Kingsbury\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c6c2c995ee001b0680d8\",\"department\":\"Costume & Make-Up\",\"job\":\"Costume Design\"},{\"adult\":false,\"gender\":0,\"id\":2622597,\"known_for_department\":\"Directing\",\"name\":\"Oliver Tummel\",\"original_name\":\"Oliver Tummel\",\"popularity\":1.38,\"profile_path\":null,\"credit_id\":\"6225cc7da7e363006c4b06f8\",\"department\":\"Directing\",\"job\":\"Second Second Assistant Director\"},{\"adult\":false,\"gender\":0,\"id\":2753693,\"known_for_department\":\"Directing\",\"name\":\"Dean Fay\",\"original_name\":\"Dean Fay\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225ccafb33903001b0a3bd4\",\"department\":\"Directing\",\"job\":\"Second Assistant Director\"},{\"adult\":false,\"gender\":0,\"id\":2894224,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Amanda Rowbottom\",\"original_name\":\"Amanda Rowbottom\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c851bccf1e0072e4de5a\",\"department\":\"Costume & Make-Up\",\"job\":\"Makeup Artist\"},{\"adult\":false,\"gender\":0,\"id\":2894224,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Amanda Rowbottom\",\"original_name\":\"Amanda Rowbottom\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c84995acf0004465ec7a\",\"department\":\"Costume & Make-Up\",\"job\":\"Hairstylist\"},{\"adult\":false,\"gender\":0,\"id\":3100299,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Andrea Cadzow\",\"original_name\":\"Andrea Cadzow\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c86607a808006adc734e\",\"department\":\"Costume & Make-Up\",\"job\":\"Hair Supervisor\"},{\"adult\":false,\"gender\":0,\"id\":3100299,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Andrea Cadzow\",\"original_name\":\"Andrea Cadzow\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c873a3770a001cc1f6e4\",\"department\":\"Costume & Make-Up\",\"job\":\"Makeup Supervisor\"},{\"adult\":false,\"gender\":0,\"id\":3136100,\"known_for_department\":\"Writing\",\"name\":\"Nick May\",\"original_name\":\"Nick May\",\"popularity\":1.548,\"profile_path\":null,\"credit_id\":\"60d5c9feef9d7200460cdc71\",\"department\":\"Writing\",\"job\":\"Screenplay\"},{\"adult\":false,\"gender\":0,\"id\":3136100,\"known_for_department\":\"Writing\",\"name\":\"Nick May\",\"original_name\":\"Nick May\",\"popularity\":1.548,\"profile_path\":null,\"credit_id\":\"60d5c9e59979d20073ed1482\",\"department\":\"Writing\",\"job\":\"Story\"},{\"adult\":false,\"gender\":0,\"id\":3136101,\"known_for_department\":\"Writing\",\"name\":\"Brandon Reavis\",\"original_name\":\"Brandon Reavis\",\"popularity\":1.96,\"profile_path\":null,\"credit_id\":\"60d5ca0825b9550046abc699\",\"department\":\"Writing\",\"job\":\"Story\"},{\"adult\":false,\"gender\":0,\"id\":3236701,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Nicole Schipilliti\",\"original_name\":\"Nicole Schipilliti\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c83d31d09b006e08d66c\",\"department\":\"Costume & Make-Up\",\"job\":\"Makeup Artist\"},{\"adult\":false,\"gender\":0,\"id\":3236701,\"known_for_department\":\"Costume & Make-Up\",\"name\":\"Nicole Schipilliti\",\"original_name\":\"Nicole Schipilliti\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c83607a808006adc7307\",\"department\":\"Costume & Make-Up\",\"job\":\"Hairstylist\"},{\"adult\":false,\"gender\":0,\"id\":3456121,\"known_for_department\":\"Production\",\"name\":\"Evan Forster\",\"original_name\":\"Evan Forster\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c75d1b7c59001b2dd552\",\"department\":\"Production\",\"job\":\"Co-Producer\"},{\"adult\":false,\"gender\":0,\"id\":3456122,\"known_for_department\":\"Production\",\"name\":\"Coco Xiaolu Ma\",\"original_name\":\"Coco Xiaolu Ma\",\"popularity\":0.6,\"profile_path\":null,\"credit_id\":\"6225c787a7e363006c4b0067\",\"department\":\"Production\",\"job\":\"Producer\"}]}}"
//    )
//    MovieAppApiTheme {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            MovieUi(movie = movie, navHostController = rememberNavController())
//        }
//    }
//}