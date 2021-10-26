package com.example.movieappapi.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.movieappapi.dataModels.AccountDetailsResponse
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.MoviesResponse
import com.example.movieappapi.dataModels.RequestTokenResponse
import com.example.movieappapi.utils.Constants
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

class Repository(private val client: HttpClient) {
    lateinit var loginResponse: RequestTokenResponse
    private var accountDetails: AccountDetailsResponse? = null

    private fun saveLoginResponse(activity: Activity) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(Constants.REQUEST_TOKEN_PREFERENCE_KEY, loginResponse.requestToken)
            putString(Constants.EXPIRE_DATE_PREFERENCE_KEY, loginResponse.expiresAt)
            putString(Constants.GUEST_SESSION_ID, loginResponse.guestSessionId)
            apply()
        }
    }

    fun getLoginResponseFromPreference(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return try {
            val expireDate = sharedPref.getString(Constants.EXPIRE_DATE_PREFERENCE_KEY, null)
            val requestToken = sharedPref.getString(Constants.REQUEST_TOKEN_PREFERENCE_KEY, null)
            val guestToken = sharedPref.getString(Constants.GUEST_SESSION_ID, null)
            if (expireDate == null)
                return false
            val calendar = getDateFromString(expireDate)
            if (Date(calendar.timeInMillis).before(Date()))
                return false
            loginResponse = RequestTokenResponse(expireDate, requestToken, guestToken, true)
            true

        } catch (exception: Exception) {
            Log.d("Repository", "getLoginResponseFromPreference: ${exception.message}")
            false
        }
    }

    private fun getDateFromString(expireDate: String): Calendar {
        val date = expireDate.substring(0, 10).trim().split("-").map { it.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, date[0])
        calendar.set(Calendar.MONTH, date[1])
        calendar.set(Calendar.DAY_OF_MONTH, date[2])
        return calendar
    }

    private suspend fun createRequestToken(): Resource<RequestTokenResponse> {
        return try {
            Resource.Success(client.get(Url.REQUEST_TOKEN))
        } catch (exception: Exception) {
            Log.d("Repository", "createRequestToken: ${exception.message}")
            exception.printStackTrace()
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun signIn(
        credentials: Credentials,
        activity: Activity
    ): Resource<RequestTokenResponse> {
        return try {
            val response = handleUserPostRequest(credentials)
            if (response is Resource.Success)
                saveLoginResponse(activity)
            response
        } catch (exception: Exception) {
            Log.d("Repository", "signIn: ${exception.message}")
            exception.printStackTrace()
            Resource.Error(exception.localizedMessage)
        }
    }

    private suspend fun handleUserPostRequest(credentials: Credentials): Resource<RequestTokenResponse> {
        val requestResponse = createRequestToken()
        return if (requestResponse is Resource.Success && requestResponse.data != null) {
            makeUserLoginPostRequest(credentials, requestResponse.data!!)
        } else
            Resource.Error(requestResponse.message)
    }

    private suspend fun makeUserLoginPostRequest(
        credentials: Credentials,
        requestResponse: RequestTokenResponse
    ): Resource<RequestTokenResponse> {
        return try {
            loginResponse = client.post<RequestTokenResponse>(Url.LOGIN_USER) {
                contentType(ContentType.Application.Json)
                body = mapOf(
                    "username" to credentials.username,
                    "password" to credentials.password,
                    "request_token" to requestResponse.requestToken
                )

            }
            if (loginResponse.success == true)
                Resource.Success(loginResponse)
            else
                Resource.Error("something went wrong")
        } catch (e: Exception) {
            Log.d("Repository", "signIn: ${e.message}")
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }

    }

    suspend fun loginAsGuest(activity: Activity): Resource<RequestTokenResponse> {
        return try {
            loginResponse = client.get(Url.GUEST_LOGIN)
            if (loginResponse.success == true) {
                saveLoginResponse(activity = activity)
                Resource.Success(loginResponse)
            } else
                Resource.Error("something went wrong")
        } catch (exception: Exception) {
            Log.d("Repository", "loginAsGuest: ${exception.message}")
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun getAccountDetails(): Resource<AccountDetailsResponse> {
        return try {
            if (loginResponse.guestSessionId != null)
                Resource.Error("currently logged in as guest")
            else {
                accountDetails = client.get(Url.ACCOUNT_DETAILS)
                Resource.Success(accountDetails!!)
            }
        } catch (exception: Exception) {
            Log.d("Repository", "getAccountDetails: ${exception.message}")
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