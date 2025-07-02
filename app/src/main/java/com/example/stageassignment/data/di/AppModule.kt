package com.example.stageassignment.data.di

import com.example.stageassignment.data.api.MockApiService
import com.example.stageassignment.data.repository.MovieRepository
import com.example.stageassignment.domain.usecase.GetMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMockApiService(): MockApiService = MockApiService()

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: MockApiService): MovieRepository = MovieRepository(apiService)

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(repository: MovieRepository): GetMoviesUseCase = GetMoviesUseCase(repository)
} 