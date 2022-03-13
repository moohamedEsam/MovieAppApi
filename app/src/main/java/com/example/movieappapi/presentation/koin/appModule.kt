package com.example.movieappapi.presentation.koin

import android.util.Log
import com.example.movieappapi.data.repository.MovieRepositoryImpl
import com.example.movieappapi.data.repository.dataSource.TMDBRemoteDataSource
import com.example.movieappapi.data.repository.dataSourceImpl.TMDBRemoteDataSourceImpl
import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.useCase.*
import com.example.movieappapi.domain.utils.Constants
import com.example.movieappapi.presentation.screen.account.AccountViewModel
import com.example.movieappapi.presentation.screen.home.MainFeedViewModel
import com.example.movieappapi.presentation.screen.login.LoginViewModel
import com.example.movieappapi.presentation.screen.movie.MovieViewModel
import com.example.movieappapi.presentation.screen.recommendation.MovieRecommendationsViewModel
import com.example.movieappapi.presentation.screen.search.SearchViewModel
import com.example.movieappapi.presentation.screen.userLists.UserListsViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val useCaseModule = module {
    single { GetNowPlayingMoviesUseCase(get()) }
    single { GetPopularMoviesUseCase(get()) }
    single { GetRecommendationsUseCase(get()) }
    single { GetTopRatedUseCase(get()) }
    single { GetSimilarMoviesUseCase(get()) }
    single { GetUpcomingMoviesUseCase(get()) }
    single { LoginUseCase(get()) }
    single { SearchAllUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    single { SearchTvUseCase(get()) }
    single { GetGenresUseCase(get()) }
    single { GetUserUseCase(get()) }
    single { UpdateCachedUser(get()) }
    single { LoginAsGuestUseCase(get()) }
    single { MarkAsFavoriteMovieUseCase(get()) }
    single { RateMovieUseCase(get()) }
}

val repositoryModule = module {
    single { provideJson() }
    single { provideHttpClient(get()) }
    single { provideMovieRemoteDataSource() }
    single { provideMovieRepository() }
}

val viewModelsModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { MainFeedViewModel(get(), get(), get()) }
    viewModel { MovieRecommendationsViewModel(get(), get()) }
    viewModel { UserListsViewModel() }
    viewModel { MovieViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { AccountViewModel(get()) }

}

private fun Scope.provideMovieRemoteDataSource(): TMDBRemoteDataSource =
    TMDBRemoteDataSourceImpl(get())

private fun Scope.provideMovieRepository(): MovieRepository = MovieRepositoryImpl(get())

fun provideHttpClient(json: Json) = HttpClient(CIO) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.i("Ktor", "log: $message")
            }
        }
    }

    install(JsonFeature) {
        serializer = KotlinxSerializer(json)
    }

    defaultRequest {
        parameter("api_key", Constants.API_KEY)
    }

}

fun provideJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
}
