package com.example.movieappapi.repository

import android.util.Log
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.MoviesResponse
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

    suspend fun register(credentials: Credentials): Resource<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(credentials.username, credentials.password).await()
            if (isUserSignedIn()) {
                auth.currentUser?.sendEmailVerification()?.await()
                return Resource.Success(Unit)
            }
            Resource.Error(unknownError)
        } catch (exception: Exception) {
            Log.d("Repository", "register: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun signIn(
        credentials: Credentials
    ): Resource<Unit> {
        return try {
            if (isUserSignedIn())
                return Resource.Error("user already signed in")
            auth.signInWithEmailAndPassword(credentials.username, credentials.password).await()
            return when {
                isUserSignedIn() && auth.currentUser?.isEmailVerified == true -> Resource.Success(
                    Unit
                )
                isUserSignedIn() -> Resource.Error("user need to authenticate the email")
                else -> Resource.Error(unknownError)
            }
        } catch (exception: Exception) {
            Log.d("Repository", "signIn: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun signInWithGoogle(token: String): Resource<Unit> {
        return try {
            val credentials = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credentials).await()
            if (isUserSignedIn())
                return Resource.Success(Unit)
            Resource.Error(unknownError)
        } catch (exception: Exception) {
            Log.d("Repository", "signInWithGoogle: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun loginAsGuest(): Resource<Unit> {
        return try {
            if (isUserSignedIn())
                return Resource.Error("user already signed in")
            auth.signInAnonymously().await()
            if (isUserSignedIn())
                Resource.Success(Unit)
            Resource.Error(unknownError)
        } catch (exception: Exception) {
            Log.d("Repository", "loginAsGuest: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }

    }


    suspend fun getPopularMovies(): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.POPULAR_MOVIES))
        } catch (exception: Exception) {
            Log.d("Repository", "getPopularMovies: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getTopRatedMovies(): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.TOP_RATED_MOVIES))
        } catch (exception: Exception) {
            Log.d("Repository", "getPopularMovies: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getNowPlayingMovies(): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.NOW_PLAYING_MOVIES))
        } catch (exception: Exception) {
            Log.d("Repository", "getPopularMovies: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getUpcomingMovies(): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.UPCOMING_MOVIES))
        } catch (exception: Exception) {
            Log.d("Repository", "getPopularMovies: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getRecommendations(movieId: Int): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.getRecommendations(movieId = movieId)))
        } catch (exception: Exception) {
            Log.d("Repository", "getRecommendations: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getSimilarMovies(movieId: Int): Resource<MoviesResponse> {
        return try {
            Resource.Success(client.get(Url.getSimilarMovies(movieId = movieId)))
        } catch (exception: Exception) {
            Log.d("Repository", "getRecommendations: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

}