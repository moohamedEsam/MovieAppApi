package com.example.movieappapi.domain.model


import com.example.movieappapi.domain.utils.customSerializers.AccountStatesRatedCustomSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountStatesResponse(
    @SerialName("favorite")
    var favorite: Boolean? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("rated")
    @Serializable(with = AccountStatesRatedCustomSerializer::class)
    var rated: Rated? = null,
    @SerialName("watchlist")
    var watchlist: Boolean? = null
)