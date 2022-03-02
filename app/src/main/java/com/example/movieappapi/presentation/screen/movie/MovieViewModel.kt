package com.example.movieappapi.presentation.screen.movie

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.domain.model.GenreResponse
import com.example.movieappapi.domain.useCase.GetGenresUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val genresUseCase: GetGenresUseCase
) : ViewModel() {
    private val _genres = mutableStateOf<GenreResponse?>(null)
    private val _isLiked = mutableStateOf<Boolean>(false)
    val isLiked: State<Boolean> = _isLiked

    init {
        setGenres()
    }

    private fun setGenres() = viewModelScope.launch {
        _genres.value = genresUseCase().data
    }

    fun getMovieGenres(ids: List<Int>): Flow<List<String>> = flow {
        val genres = mutableListOf<String>()
        _genres.value?.genres?.let {
            for (i in it.indices) {
                if (it[i].id in ids)
                    genres.add(it[i].name ?: "")
            }
        }
        emit(genres.toList())
    }




}