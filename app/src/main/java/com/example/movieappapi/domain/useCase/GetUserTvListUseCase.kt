package com.example.movieappapi.domain.useCase

import com.example.movieappapi.domain.repository.MovieRepository
import com.example.movieappapi.domain.utils.UserTvList

class GetUserTvListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(list: UserTvList) = when (list) {
        is UserTvList.FavoriteTv -> repository.getUserFavoriteTv()
        is UserTvList.TvWatchlist -> repository.getUserTvWatchList()
        else -> repository.getUserRatedTv()
    }
}