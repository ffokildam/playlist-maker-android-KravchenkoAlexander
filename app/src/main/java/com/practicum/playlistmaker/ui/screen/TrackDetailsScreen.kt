package com.practicum.playlistmaker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.ui.presentation.PlaylistsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel,
    navigateBack: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    var showPlaylistSheet by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(track.favorite) }
    var currentTrack by remember { mutableStateOf(track) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(track) {
        val existing = playlistsViewModel.isExist(track)
        existing?.let {
            currentTrack = it
            isFavorite = it.favorite
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navigateBack() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (track.artworkUrl100 != null) {
                        AsyncImage(
                            model = track.artworkUrl100,
                            contentDescription = track.trackName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(120.dp),
                            painter = painterResource(id = R.drawable.ic_playlist),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = track.trackName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = track.artistName,
                    fontSize = 16.sp,
                    color = Color(0xFF444444)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(
                        onClick = { showPlaylistSheet = true },
                        modifier = Modifier
                            .size(60.dp)
                            .background(color = Color(0xFFE0E0E0), shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_to_playlist),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.duration),
                        fontSize = 12.sp,
                        color = Color(0xFFB0B0B0)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            scope.launch {
                                playlistsViewModel.toggleFavorite(currentTrack, isFavorite)
                            }
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .background(color = Color(0xFFE0E0E0), shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_favorite_filled),
                            contentDescription = null,
                            tint = if (isFavorite) Color.Red else Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = track.trackTime,
                        fontSize = 12.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }

    if (showPlaylistSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPlaylistSheet = false },
            modifier = Modifier.fillMaxHeight(0.7f),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_playlist),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(playlists) { playlist ->
                        PlaylistSelectionItem(
                            playlist = playlist,
                            onClick = {
                                scope.launch {
                                    playlistsViewModel.insertTrackToPlaylist(currentTrack, playlist.id)
                                    showPlaylistSheet = false
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


@Composable
fun PlaylistSelectionItem(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.ic_playlist),
            contentDescription = playlist.name,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(playlist.name, fontSize = 16.sp)
            Text(
                text = "${playlist.tracks.size} tracks",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

