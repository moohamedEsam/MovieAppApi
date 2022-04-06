package com.example.movieappapi.domain.utils

interface BasePaginationUseCase<T> {
    var page: Int
    var endReached: Boolean

    suspend fun request(onRequest: suspend () -> Resource<T>): Resource<T> {
        val resource = onRequest()
        if (resource is Resource.Success)
            page++
        return resource
    }

    suspend fun reset() {
        page = 1
    }
}