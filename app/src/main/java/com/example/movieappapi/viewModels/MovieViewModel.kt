package com.example.movieappapi.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappapi.dataModels.GenreResponse
import com.example.movieappapi.dataModels.Movie
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: Repository) : ViewModel() {
    private val _genres = mutableStateOf<GenreResponse?>(null)
    private val _isLiked = mutableStateOf(false)
    val isLiked: State<Boolean> = _isLiked

    init {
        setGenres()

    }

    private fun setGenres() = viewModelScope.launch {
        if (repository.genreResponse == null)
            repository.getGenres()
        _genres.value = repository.genreResponse
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

    fun likeMovie(movie: Movie) = viewModelScope.launch {
        repository.likeMovie(movie = movie)
    }

    fun unlikeMovie(movie: Movie) = viewModelScope.launch {
        repository.unlikeMovie(movie = movie)
    }

    fun isLiked(movie: Movie) = viewModelScope.launch {
        val result = repository.isMovieLiked(movie = movie)
        if (result is Resource.Success)
            _isLiked.value = result.data ?: false
    }

    fun setIsLiked(value: Boolean) {
        _isLiked.value = value
    }

}