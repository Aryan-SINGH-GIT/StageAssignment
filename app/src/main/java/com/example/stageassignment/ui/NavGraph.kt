package com.example.stageassignment.ui

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.stageassignment.presentation.viewmodel.MovieUiState
import com.example.stageassignment.presentation.viewmodel.MovieViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.example.stageassignment.ui.theme.AppBackground
import com.example.stageassignment.ui.theme.AppPrimary
import com.example.stageassignment.ui.theme.AppOnBackground
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.example.stageassignment.R
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color
import com.example.stageassignment.ui.theme.AppSecondary
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

import androidx.compose.foundation.focusable


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.onFocusChanged
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import android.app.Activity

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movie_list") {
        composable("movie_list") { MovieListScreen(navController) }
        composable("movie_details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailsScreen(navController, movieId)
        }
        composable("player/{videoUrl}") { backStackEntry ->
            val videoUrl = Uri.decode(backStackEntry.arguments?.getString("videoUrl") ?: "")
            PlayerScreen(navController, videoUrl)
        }
    }
}

@Composable
fun MovieListScreen(navController: NavHostController) {
    val viewModel: MovieViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Navigation Rail (icons only, left side)
            Column(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(AppBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val context = LocalContext.current
                val activity = context as? Activity
                Spacer(modifier = Modifier.height(32.dp))
                IconButton(
                    onClick = { activity?.finish() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = AppOnBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            // Main content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                when (uiState) {
                    is MovieUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppPrimary)
                    }
                    is MovieUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.message, color = AppOnBackground)
                    }
                    is MovieUiState.Success -> {
                        val movies = uiState.movies
                        val selectedMovie = movies.getOrNull(0)
                        if (selectedMovie != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                                    .background(AppBackground)
                                    .padding(horizontal = 32.dp, vertical = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Text content
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 32.dp)
                                ) {
                                    Text(
                                        text = selectedMovie.title,
                                        color = AppOnBackground,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "FILM • CRIME • DRAMA",
                                        color = AppPrimary,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = selectedMovie.description,
                                        color = AppOnBackground,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 3
                                    )
                                }
                                // Poster image
                                Image(
                                    painter = rememberAsyncImagePainter(selectedMovie.thumbnailUrl),
                                    contentDescription = selectedMovie.title,
                                    modifier = Modifier
                                        .height(200.dp)
                                        .aspectRatio(0.7f)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Movie row
                        Text(
                            text = "Movies",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                        )
                        LazyRow(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)) {
                            items(movies) { movie ->
                                Card(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .padding(8.dp)
                                        .clickable {
                                            navController.navigate("movie_details/${movie.id}")
                                        },
                                    colors = CardDefaults.cardColors(containerColor = AppBackground),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(movie.thumbnailUrl),
                                            contentDescription = movie.title,
                                            modifier = Modifier
                                                .height(180.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = movie.title,
                                            color = AppOnBackground,
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailsScreen(navController: NavHostController, movieId: String) {
    val viewModel: MovieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val movie = (uiState as? MovieUiState.Success)?.movies?.find { it.id == movieId }

    Surface(color = AppBackground, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is MovieUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }
            is MovieUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = (uiState as MovieUiState.Error).message, color = AppOnBackground)
                }
            }
            is MovieUiState.Success -> {
                if (movie == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Movie not found", color = AppOnBackground)
                    }
                    return@Surface
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(movie.thumbnailUrl),
                            contentDescription = movie.title,
                            modifier = Modifier
                                .size(220.dp, 330.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = movie.title,
                            color = AppOnBackground,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = movie.description,
                            color = AppOnBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "IMDb: ${movie.imdbRating}",
                            color = AppPrimary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigate("player/${Uri.encode(movie.videoUrl)}") },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AppPrimary)
                        ) {
                            Text(text = "Play", color = AppOnBackground)
                        }
                    }
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = AppOnBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerScreen(navController: NavHostController, videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }
    BackHandler { navController.popBackStack() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
} 