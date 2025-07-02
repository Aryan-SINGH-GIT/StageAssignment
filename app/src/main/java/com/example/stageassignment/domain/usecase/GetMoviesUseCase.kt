package com.example.stageassignment.domain.usecase

import com.example.stageassignment.data.model.Movie
import com.example.stageassignment.data.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(): List<Movie> = repository.getMovies()
} 