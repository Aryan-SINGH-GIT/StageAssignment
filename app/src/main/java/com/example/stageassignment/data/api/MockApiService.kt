package com.example.stageassignment.data.api

import javax.inject.Inject
import com.example.stageassignment.data.model.Movie
import kotlinx.coroutines.delay

class MockApiService @Inject constructor() {
    suspend fun getMovies(): List<Movie> {
        delay(1000) // Simulate network delay
        return listOf(
            Movie(
                id = "1",
                title = "Bajri Mafia",
                description = "A gritty crime drama set in the world of illegal sand mining mafias, where power, betrayal, and survival collide.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BNWFkYjNhZGMtZjllZC00ODQ0LWExOGMtZmMyNzJkZDBhMzMzXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                imdbRating = 8.2
            ),
            Movie(
                id = "2",
                title = "Crime haryana",
                description = "A suspenseful tale of a detective unraveling a complex murder mystery in the heart of the city.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BMjkxMDI1M2ItNDIwZC00MTY5LTg2NzQtYjlhOGI3M2Y1NDkwXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                imdbRating = 7.8
            ),
            Movie(
                id = "3",
                title = "Desi Pnchayat",
                description = "A lighthearted comedy about Indian friends navigating life, love, and culture shock in Australia.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BODFmMGRkY2QtNzg5OC00ZDRlLWJmNTMtYTRlYWRlMmYxYzMyXkEyXkFqcGc@._V1_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                imdbRating = 7.5
            ),
            Movie(
                id = "4",
                title = "DAAM",
                description = "A psychological thriller exploring the price of ambition and the dark secrets people keep.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BNTA3MjdhMDQtOGM1MS00Zjk0LWJlM2MtZDdhZDM3Yjc5NWE4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                imdbRating = 8.0
            ),
            Movie(
                id = "5",
                title = "Prem Kabootar",
                description = "A heartwarming romance where love letters travel across cities, carried by a faithful pigeon.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BNTA3MjdhMDQtOGM1MS00Zjk0LWJlM2MtZDdhZDM3Yjc5NWE4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                imdbRating = 7.2
            ),
            Movie(
                id = "6",
                title = "Shiva Ke Ram Bhoomi",
                description = "A mythological epic chronicling Lord Shiva's journey and his connection to the sacred land of Ram Bhoomi.",
                thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BYTBiNGFiNWYtYzE1OS00ZTM1LWI4YTEtOTg4YTU1ZjE0YTNiXkEyXkFqcGc@._V1_.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                imdbRating = 8.5
            ),
            Movie(
                    id = "3",
            title = "Desi Pnchayat",
            description = "A lighthearted comedy about Indian friends navigating life, love, and culture shock in Australia.",
            thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BODFmMGRkY2QtNzg5OC00ZDRlLWJmNTMtYTRlYWRlMmYxYzMyXkEyXkFqcGc@._V1_.jpg",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            imdbRating = 7.5
            ),
            Movie(
            id = "4",
            title = "DAAM",
            description = "A psychological thriller exploring the price of ambition and the dark secrets people keep.",
            thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BNTA3MjdhMDQtOGM1MS00Zjk0LWJlM2MtZDdhZDM3Yjc5NWE4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            imdbRating = 8.0
            ),
            Movie(
            id = "5",
            title = "Prem Kabootar",
            description = "A heartwarming romance where love letters travel across cities, carried by a faithful pigeon.",
            thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BNTA3MjdhMDQtOGM1MS00Zjk0LWJlM2MtZDdhZDM3Yjc5NWE4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            imdbRating = 7.2
            ),
            Movie(
            id = "6",
            title = "Shiva Ke Ram Bhoomi",
            description = "A mythological epic chronicling Lord Shiva's journey and his connection to the sacred land of Ram Bhoomi.",
            thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BYTBiNGFiNWYtYzE1OS00ZTM1LWI4YTEtOTg4YTU1ZjE0YTNiXkEyXkFqcGc@._V1_.jpg",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            imdbRating = 8.5
            )
        )

    }
} 