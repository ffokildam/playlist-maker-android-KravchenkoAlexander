package com.practicum.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.presentation.TrackScreenState
import com.practicum.playlistmaker.presentation.TrackViewModel
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

class TrackActivity : ComponentActivity() {
    private val viewModel: TrackViewModel by viewModels { TrackViewModel.getViewModelFactory("123") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlaylistMakerTheme {
                TrackScreen(viewModel)
            }
        }
    }
}

@Composable
fun TrackScreen(viewModel: TrackViewModel) {
    //1
    val screenState by viewModel.trackScreenState.collectAsState()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (screenState) { // 2
            is TrackScreenState.Content -> {
                TrackScreenContent(viewModel, screenState as TrackScreenState.Content) //1
            }

            is TrackScreenState.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun TrackScreenContent(viewModel: TrackViewModel, screenState: TrackScreenState.Content) {
    val playerStatus by viewModel.playerStatusState.collectAsState() //2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Здесь можно добавить AsyncImage из Coil для загрузки изображений
        // Пока используем заглушку
        Text(
            text = "Cover Image: ${screenState.trackModel.pictureUrl}",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = screenState.trackModel.author,
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = screenState.trackModel.name,
            style = MaterialTheme.typography.bodyLarge
        )

        val buttonText = if (playerStatus.isPlaying) "Pause" else "Play" // 3
        Button(
            onClick = {
                if (playerStatus.isPlaying) {
                    viewModel.pause()
                } else {
                    viewModel.play()
                }
            }
        ) {
            Text(buttonText) // 4
        }

        Slider(
            value = playerStatus.progress,
            onValueChange = { viewModel.seek(it) } //5
        )
    }
}

