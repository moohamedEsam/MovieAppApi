package com.example.movieappapi.repository

import android.util.Log
import com.example.movieappapi.dataModels.*
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
    private val root = firestore.collection("users")
    var genreResponse: GenreResponse? = null

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    private fun getCurrentUser() = auth.currentUser?.uid ?: ""

    private suspend fun <T> baseTry(code: suspend () -> Resource<T>): Resource<T> {
        return try {
            code()
        } catch (exception: Exception) {
            Log.d("Repository", "${exception.stackTrace[1].methodName}: ${exception.message}")
            Resource.Error(exception.localizedMessage)
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

    suspend fun likeMovie(movie: Movie) {
        baseTry {
            root.document(getCurrentUser())
                .collection("likes")
                .document("${movie.id}")
                .set(movie).await()
            Resource.Success(Unit)
        }
    }

    suspend fun isMovieLiked(movie: Movie) = try {
        val result = root.document(getCurrentUser())
            .collection("likes")
            .document("${movie.id}")
            .get().await().exists()

        Resource.Success(result)
    } catch (exception: Exception) {
        Log.d("Repository", "isMovieLiked: ${exception.message}")
        Resource.Error(exception.message)
    }

    suspend fun unlikeMovie(movie: Movie) {
        baseTry {
            root.document(getCurrentUser())
                .collection("likes")
                .document("${movie.id}")
                .delete().await()
            Resource.Initialized<Unit>()
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

    private suspend inline fun <reified T> baseSearch(url: String, query: String): Resource<T> =
        baseTry {
            Resource.Success(
                client.get(url) {
                    parameter("query", query)
                }
            )
        }

    suspend fun searchAll(query: String): Resource<AllSearchResponse> =
        baseSearch(Url.SEARCH_ALL, query)


    suspend fun searchMovie(query: String): Resource<MoviesResponse> =
        baseSearch(Url.SEARCH_MOVIE, query)

    suspend fun searchTv(query: String): Resource<TvShowsResponse> =
        baseSearch(Url.SEARCH_TV, query)

    suspend fun getGenres() {
        baseTry {
            genreResponse = client.get<GenreResponse>(Url.GENRES)
            Resource.Success(Unit)
        }
    }
}