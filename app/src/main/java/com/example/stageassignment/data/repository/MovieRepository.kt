package com.example.stageassignment.data.repository

import com.example.stageassignment.data.api.MockApiService
import com.example.stageassignment.data.model.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: MockApiService) {
    suspend fun getMovies(): List<Movie> = apiService.getMovies()
} 