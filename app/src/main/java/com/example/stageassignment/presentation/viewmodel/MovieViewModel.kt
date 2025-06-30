package com.example.stageassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stageassignment.data.model.Movie
import com.example.stageassignment.data.api.MockApiService
import com.example.stageassignment.data.repository.MovieRepository
import com.example.stageassignment.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MovieViewModel : ViewModel() {
    private val apiService = MockApiService()
    private val repository = MovieRepository(apiService)
    private val getMoviesUseCase = GetMoviesUseCase(repository)

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val movies = getMoviesUseCase()
                _uiState.value = MovieUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 