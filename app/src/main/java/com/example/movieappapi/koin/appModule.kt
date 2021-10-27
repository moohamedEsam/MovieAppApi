package com.example.movieappapi.koin

import android.util.Log
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Constants
import com.example.movieappapi.viewModels.LoginViewModel
import com.example.movieappapi.viewModels.MainFeedViewModel
import com.example.movieappapi.viewModels.MovieRecommendationsViewModel
import com.example.movieappapi.viewModels.UserListsViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single { provideJson() }
    single { provideHttpClient(get()) }
    single { Repository(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainFeedViewModel(get()) }
    viewModel { MovieRecommendationsViewModel(get()) }
    viewModel { UserListsViewModel(get()) }
}

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
