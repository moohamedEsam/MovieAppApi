package com.example.movieappapi.repository

import android.util.Log
import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.dataModels.RequestTokenResponse
import com.example.movieappapi.utils.Resource
import com.example.movieappapi.utils.Url
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class Repository(private val client: HttpClient) {

    private suspend fun createRequestToken(): Resource<RequestTokenResponse> {
        return try {
            Resource.Success(client.get(Url.REQUEST_TOKEN))
        } catch (exception: Exception) {
            Log.d("Repository", "createRequestToken: ${exception.message}")
            exception.printStackTrace()
            Resource.Error(exception.localizedMessage)
        }
    }

    suspend fun signIn(credentials: Credentials): Resource<RequestTokenResponse> {
        return try {
            handleUserPostRequest(credentials)
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
            val response = client.post<RequestTokenResponse>(Url.LOGIN_USER) {
                contentType(ContentType.Application.Json)
                body = mapOf(
                    "username" to credentials.username,
                    "password" to credentials.password,
                    "request_token" to requestResponse.requestToken
                )

            }
            if (response.success == true)
                Resource.Success(response)
            else
                Resource.Error("something went wrong")
        } catch (e: Exception) {
            Log.d("Repository", "signIn: ${e.message}")
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }

    }
}