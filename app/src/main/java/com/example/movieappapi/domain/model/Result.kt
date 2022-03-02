package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("adult")
    var adult: Boolean? = null,
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    @SerialName("first_air_date")
    var firstAirDate: String? = null,
    @SerialName("genre_ids")
    var genreIds: List<Int>? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("known_for")
    var knownFor: List<KnownFor>? = null,
    @SerialName("media_type")
    var mediaType: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("origin_country")
    var originCountry: List<String>? = null,
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_name")
    var originalName: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    @SerialName("overview")
    var overview: String? = null,
    @SerialName("popularity")
    var popularity: Float? = null,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("profile_path")
    var profilePath: String? = null,
    @SerialName("release_date")
    var releaseDate: String? = null,
    @SerialName("title")
    var title: String? = null,
    @SerialName("video")
    var video: Boolean? = null,
    @SerialName("vote_average")
    var voteAverage: Float? = null,
    @SerialName("vote_count")
    var voteCount: Float? = null
)