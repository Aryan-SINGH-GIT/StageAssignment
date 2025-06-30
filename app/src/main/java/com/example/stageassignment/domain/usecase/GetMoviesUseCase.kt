package com.example.stageassignment.domain.usecase

import com.example.stageassignment.data.model.Movie
import com.example.stageassignment.data.repository.MovieRepository
 
class GetMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(): List<Movie> = repository.getMovies()
} 