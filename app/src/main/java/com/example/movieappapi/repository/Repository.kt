package com.example.movieappapi.repository

import android.util.Log
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.RequestTokenResponse
import com.example.movieappapi.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*

class Repository(private val client: HttpClient) {

    private suspend fun createRequestToken(): RequestTokenResponse? {
        return try {
            client.get(Url.REQUEST_TOKEN)
        } catch (exception: Exception) {
            Log.d("Repository", "createRequestToken: ${exception.message}")
            exception.printStackTrace()
            null
        }
    }

    suspend fun signIn(credentials: Credentials): Boolean {
        return try {
            handleUserPostRequest(credentials)
        } catch (exception: Exception) {
            Log.d("Repository", "signIn: ${exception.message}")
            exception.printStackTrace()
            false
        }
    }

    private suspend fun handleUserPostRequest(credentials: Credentials): Boolean {
        val requestResponse = createRequestToken()
        return if (requestResponse?.success == true && requestResponse.requestToken != null) {
            makeUserLoginPostRequest(credentials, requestResponse)
        }
        else
            false
    }

    private suspend fun makeUserLoginPostRequest(
        credentials: Credentials,
        requestResponse: RequestTokenResponse
    ): Boolean {
        val response = client.post<RequestTokenResponse>(Url.LOGIN_USER) {
            body = mapOf(
                "username" to credentials.username,
                "password" to credentials.password,
                "request_token" to requestResponse.requestToken
            )
        }
        return response.success ?: false
    }
}