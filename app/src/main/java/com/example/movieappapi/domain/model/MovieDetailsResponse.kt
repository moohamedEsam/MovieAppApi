package com.example.movieappapi.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("adult")
    var adult: Boolean? = null,
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    @SerialName("belongs_to_collection")
    var belongsToCollection: BelongsToCollection? = null,
    @SerialName("budget")
    var budget: Int? = null,
    @SerialName("genres")
    var genres: List<Genre>? = null,
    @SerialName("homepage")
    var homepage: String? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("imdb_id")
    var imdbId: String? = null,
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    @SerialName("overview")
    var overview: String? = null,
    @SerialName("popularity")
    var popularity: Double? = null,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("production_companies")
    var productionCompanies: List<ProductionCompany>? = null,
    @SerialName("production_countries")
    var productionCountries: List<ProductionCountry>? = null,
    @SerialName("release_date")
    var releaseDate: String? = null,
    @SerialName("revenue")
    var revenue: Int? = null,
    @SerialName("runtime")
    var runtime: Int? = null,
    @SerialName("spoken_languages")
    var spokenLanguages: List<SpokenLanguage>? = null,
    @SerialName("status")
    var status: String? = null,
    @SerialName("tagline")
    var tagline: String? = null,
    @SerialName("title")
    var title: String? = null,
    @SerialName("video")
    var video: Boolean? = null,
    @SerialName("vote_average")
    var voteAverage: Double? = null,
    @SerialName("vote_count")
    var voteCount: Int? = null,
    @SerialName("account_states")
    var accountStatesResponse: AccountStatesResponse? = null,
    @SerialName("credits")
    var movieCredits: MovieCredits? = null,
    @SerialName("keywords")
    var keywords: KeywordResponse? = null
)