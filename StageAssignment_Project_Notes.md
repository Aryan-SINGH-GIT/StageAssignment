# StageAssignment Android TV App - Project Notes

## Overview
This project is a modern Android TV application built with Kotlin, Jetpack Compose, and MVVM architecture. The app displays a list of movies from a mock API, allows users to view movie details, and play videos using ExoPlayer. The UI is optimized for TV screens and remote navigation.

---

## Features
- Movie list with horizontal scrolling (LazyRow)
- Movie details screen with poster, title, description, and IMDb rating
- Video playback using ExoPlayer
- TV-optimized navigation and layout
- Customizable mock data (titles, descriptions, images, ratings)
- Modern dark theme with red and white accents
- Back button to exit the app

---

## Architecture
- **MVVM Pattern**: Separation of data, domain, and presentation layers
    - `data/`: Model, API, Repository
    - `domain/`: UseCase
    - `presentation/`: ViewModel
    - `ui/`: Screens, navigation, theme
- **Jetpack Compose**: Declarative UI for all screens
- **Navigation**: Compose Navigation for screen transitions

---

## Libraries Used
- **Jetpack Compose** (UI, Navigation, Tooling)
- **ExoPlayer** (Video playback)
- **Coil** (Image loading)
- **AndroidX TV Foundation/Material** (TV-optimized components)
- **Kotlin Coroutines** (Async data loading)
- **Lifecycle/ViewModel** (State management)

---

## UI/UX Design
- **Dark theme**: Black background, white text, red accent
- **Banner**: Large movie banner with poster, title, genre, and description
- **Movie Row**: Horizontally scrollable cards with poster and title
- **Details Screen**: Poster, title, description, IMDb rating, and Play button
- **Player Screen**: Fullscreen video with black background
- **Back Button**: Top-left, exits the app

---

## Mock API/Data
- Movies are provided by `MockApiService` with:
    - Title, description, poster URL, video URL, IMDb rating
- Poster and video URLs are real and public
- Descriptions are unique and relevant to each title

---

## Navigation
- **NavGraph**: Handles navigation between movie list, details, and player screens
- **Back navigation**: System back and custom back button supported

---

## ExoPlayer Integration
- ExoPlayer is used for video playback in the player screen
- Handles play, pause, and seek
- Uses HTTPS video URLs for compatibility

---

## TV Focus/Navigation
- UI is optimized for TV remote navigation
- Movie cards are clickable; D-pad navigation is supported by default in Compose TV components
- No custom focus outline (as per final requirements)

---

## How to Run
1. Open the project in Android Studio (Giraffe or newer recommended)
2. Sync Gradle to download all dependencies
3. Build and run on an Android TV emulator or device
4. Use the D-pad or remote to navigate, select movies, and play videos

---

## Customization Tips
- **Add more movies**: Edit `MockApiService.kt` to add or update movies
- **Change theme**: Edit `ui/theme/Color.kt` and `Theme.kt`
- **Update navigation**: Edit `ui/NavGraph.kt` for new screens or flows
- **Improve TV focus**: Use `TvLazyRow` and TV Material components for advanced focus if needed

---

## Credits
- Sample video URLs: Google GTV sample videos
- Sample poster images: IMDb and other public sources

---

**Project by: [Your Name/Team]** 