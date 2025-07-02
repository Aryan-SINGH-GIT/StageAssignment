package com.example.stageassignment.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.stageassignment.presentation.viewmodel.MovieUiState
import com.example.stageassignment.presentation.viewmodel.MovieViewModel
import com.example.stageassignment.ui.theme.AppBackground
import com.example.stageassignment.ui.theme.AppPrimary
import com.example.stageassignment.ui.theme.AppOnBackground
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.example.stageassignment.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.focusable
import androidx.compose.runtime.LaunchedEffect
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Card as TvCard
import androidx.tv.material3.CardDefaults as TvCardDefaults
import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Slider

import kotlinx.coroutines.delay

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.viewinterop.AndroidView

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView


import androidx.tv.material3.IconButton as TvIconButton


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


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavHostController) {
    val viewModel: MovieViewModel = hiltViewModel()
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
                        val selectedMovie = movies.getOrNull((1..6).random())
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
                            color = AppOnBackground,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                        )
                        val focusRequester = remember { FocusRequester() }
                        var hasRequestedFocus by remember { mutableStateOf(false) }

                        TvLazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            itemsIndexed(movies) { index, movie ->
                                val isFirst = index == 0
                                val itemFocusRequester = if (isFirst) focusRequester else remember { FocusRequester() }

                                val cardModifier = Modifier
                                    .width(140.dp)
                                    .padding(8.dp)
                                    .then(
                                        if (isFirst) {
                                            Modifier
                                                .focusRequester(itemFocusRequester)
                                                .focusable()
                                                .onFocusChanged {
                                                    if (it.isFocused) {
                                                        // You can log or trigger any events here
                                                    }
                                                }
                                        } else Modifier
                                    )

                                TvCard(
                                    onClick = { navController.navigate("movie_details/${movie.id}") },
                                    modifier = cardModifier,
                                    colors = TvCardDefaults.colors(containerColor = AppBackground),
                                    scale = TvCardDefaults.scale(focusedScale = 1.1f),
                                    border = TvCardDefaults.border(focusedBorder = Border(BorderStroke(2.dp, AppPrimary))),
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

                                // ✅ Safely request focus **after** layout
                                if (isFirst && !hasRequestedFocus) {
                                    LaunchedEffect(Unit) {
                                        // Wait for frame draw
                                        snapshotFlow { hasRequestedFocus }.collect {
                                            if (!hasRequestedFocus) {
                                                itemFocusRequester.requestFocus()
                                                hasRequestedFocus = true
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
    }
}

@Composable
fun MovieDetailsScreen(navController: NavHostController, movieId: String) {
    val viewModel: MovieViewModel = hiltViewModel()
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

@OptIn(ExperimentalTvMaterial3Api::class)
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

    var isPlaying by remember { mutableStateOf(true) }
    var controllerVisible by remember { mutableStateOf(true) }
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    val SEEK_AMOUNT = 10_000L // 10 seconds
    val sliderFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(200)
        controllerVisible = true
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            position = exoPlayer.currentPosition
            duration = exoPlayer.duration.coerceAtLeast(1L)
            delay(500)
        }
    }

    LaunchedEffect(controllerVisible) {
        if (controllerVisible) {
            delay(200) // Give Compose time to lay out
            sliderFocusRequester.requestFocus()
        }
    }

    BackHandler { navController.popBackStack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { controllerVisible = true }
    ) {
        // 🔹 ExoPlayer Video View
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (controllerVisible) {
            // 🔹 Playback Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(Color(0xAA000000), RoundedCornerShape(32.dp))
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    TvIconButton(
                        onClick = {
                            val back = exoPlayer.currentPosition - exoPlayer.seekBackIncrement
                            if (duration > 0L) exoPlayer.seekTo(back.coerceAtLeast(0L))
                            controllerVisible = true
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.FastRewind, contentDescription = "Rewind", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    TvIconButton(
                        onClick = {
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                                isPlaying = false
                            } else {
                                exoPlayer.play()
                                isPlaying = true
                            }
                            controllerVisible = true
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                if (exoPlayer.isPlaying) {
                                    exoPlayer.pause()
                                    isPlaying = false
                                } else {
                                    exoPlayer.play()
                                    isPlaying = true
                                }
                                controllerVisible = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    TvIconButton(
                        onClick = {
                            val forward = exoPlayer.currentPosition + exoPlayer.seekForwardIncrement
                            if (duration > 0L) exoPlayer.seekTo(forward.coerceAtMost(duration))
                            controllerVisible = true
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.FastForward, contentDescription = "Forward", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Slider and Time
                val safeDuration = duration.coerceAtLeast(1L)
                val safePosition = position.coerceAtMost(safeDuration)

                var sliderPosition by remember { mutableStateOf(0f) }
                var isSeeking by remember { mutableStateOf(false) }

                // Keep slider in sync with player unless user is seeking
                LaunchedEffect(position, isSeeking) {
                    if (!isSeeking) sliderPosition = safePosition.toFloat()
                }

                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        isSeeking = true
                    },
                    onValueChangeFinished = {
                        exoPlayer.seekTo(sliderPosition.toLong())
                        isSeeking = false
                    },
                    valueRange = 0f..safeDuration.toFloat(),
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(sliderFocusRequester)
                        .focusable()
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                when (event.key) {
                                    Key.DirectionLeft -> {
                                        val newPos = (sliderPosition - SEEK_AMOUNT).coerceAtLeast(0f)
                                        sliderPosition = newPos
                                        exoPlayer.seekTo(newPos.toLong())
                                        isSeeking = false
                                        true
                                    }
                                    Key.DirectionRight -> {
                                        val newPos = (sliderPosition + SEEK_AMOUNT).coerceAtMost(safeDuration.toFloat())
                                        sliderPosition = newPos
                                        exoPlayer.seekTo(newPos.toLong())
                                        isSeeking = false
                                        true
                                    }
                                    Key.Enter, Key.NumPadEnter, Key.Spacebar -> {
                                        exoPlayer.seekTo(sliderPosition.toLong())
                                        isSeeking = false
                                        true
                                    }
                                    else -> false
                                }
                            } else false
                        }
                )

                val formattedPosition = String.format("%02d:%02d", (position / 1000) / 60, (position / 1000) % 60)
                val formattedDuration = String.format("%02d:%02d", (duration / 1000) / 60, (duration / 1000) % 60)

                Text(
                    text = "$formattedPosition / $formattedDuration",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.End).padding(end = 16.dp)
                )
            }
        }
    }
}
