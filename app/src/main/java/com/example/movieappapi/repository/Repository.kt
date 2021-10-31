package com.example.movieappapi.repository

import com.example.movieappapi.dataModels.AllSearchResponse
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.dataModels.TvShowsResponse
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Url
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.tasks.await

class Repository(
    private val client: HttpClient,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val unknownError = "something went wrong"
    val root = firestore.collection("root")

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    private suspend fun <T> baseTry(code: suspend () -> Resource<T>): Resource<T> {
        return baseTry {
            code()
        }
    }

    suspend fun register(credentials: Credentials): Resource<Unit> {
        return baseTry {
            auth.createUserWithEmailAndPassword(credentials.username, credentials.password).await()
            if (isUserSignedIn()) {
                auth.currentUser?.sendEmailVerification()?.await()
                Resource.Success(Unit)
            }
            Resource.Error(unknownError)
        }
    }

    suspend fun signIn(
        credentials: Credentials
    ): Resource<Unit> {
        return baseTry {
            if (isUserSignedIn())
                Resource.Error<Unit>("user already signed in")
            auth.signInWithEmailAndPassword(credentials.username, credentials.password).await()
            when {
                isUserSignedIn() && auth.currentUser?.isEmailVerified == true -> Resource.Success(
                    Unit
                )
                isUserSignedIn() -> Resource.Error("user need to authenticate the email")
                else -> Resource.Error(unknownError)
            }
        }
    }

    suspend fun signInWithGoogle(token: String): Resource<Unit> {
        return baseTry {
            val credentials = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credentials).await()
            if (isUserSignedIn())
                Resource.Success(Unit)
            Resource.Error(unknownError)
        }
    }

    suspend fun loginAsGuest(): Resource<Unit> {
        return baseTry {
            if (isUserSignedIn())
                Resource.Error<Unit>("user already signed in")
            auth.signInAnonymously().await()
            if (isUserSignedIn())
                Resource.Success(Unit)
            Resource.Error(unknownError)
        }
    }


    suspend fun getPopularMovies(): Resource<MoviesResponse> = baseTry {
        Resource.Success(client.get(Url.POPULAR_MOVIES))
    }


    suspend fun getTopRatedMovies(): Resource<MoviesResponse> = baseTry {
        Resource.Success(client.get(Url.TOP_RATED_MOVIES))
    }


    suspend fun getNowPlayingMovies(): Resource<MoviesResponse> = baseTry {
        Resource.Success(client.get(Url.NOW_PLAYING_MOVIES))
    }


    suspend fun getUpcomingMovies(): Resource<MoviesResponse> =
        baseTry {
            Resource.Success(client.get(Url.UPCOMING_MOVIES))
        }


    suspend fun getRecommendations(movieId: Int): Resource<MoviesResponse> = baseTry {
        Resource.Success(client.get(Url.getRecommendations(movieId = movieId)))
    }


    suspend fun getSimilarMovies(movieId: Int): Resource<MoviesResponse> = baseTry {
        Resource.Success(client.get(Url.getSimilarMovies(movieId = movieId)))
    }

    private suspend fun <T> baseSearch(url: String, query: String): Resource<T> = baseTry {
        client.get(url) {
            parameter("query", query)
        }
    }

    suspend fun searchAll(query: String): Resource<AllSearchResponse> =
        baseSearch(Url.SEARCH_ALL, query)


    suspend fun searchMovie(query: String): Resource<MoviesResponse> =
        baseSearch(Url.SEARCH_MOVIE, query)

    suspend fun searchTv(query: String): Resource<TvShowsResponse> =
        baseSearch(Url.SEARCH_TV, query)


}