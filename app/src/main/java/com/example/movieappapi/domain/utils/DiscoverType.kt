package com.example.movieappapi.domain.utils

sealed class DiscoverType(val param: String) {
    object Keyword : DiscoverType("with_keywords")
    object Genre : DiscoverType("with_genres")
    object Cast : DiscoverType("with_cast")
}
