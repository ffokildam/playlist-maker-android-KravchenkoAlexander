package com.practicum.playlistmaker.ui.screen

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.ui.presentation.PlaylistsViewModel
import kotlinx.coroutines.launch

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    var showMergeBottomSheet by remember { mutableStateOf(false) }
    var selectedPlaylistForMerge by remember { mutableStateOf<Playlist?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewPlaylist() },
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_playlist),
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navigateBack() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.playlist),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistListItem(
                        playlist = playlist,
                        onClick = { navigateToPlaylist(playlist.id) },
                        onLongClick = {
                            selectedPlaylistForMerge = playlist
                            showMergeBottomSheet = true
                        }
                    )
                }
            }
        }
        
        if (showMergeBottomSheet && selectedPlaylistForMerge != null) {
            MergePlaylistBottomSheet(
                sourcePlaylist = selectedPlaylistForMerge!!,
                allPlaylists = playlists.filter { it.id != selectedPlaylistForMerge!!.id },
                onDismiss = {
                    showMergeBottomSheet = false
                    selectedPlaylistForMerge = null
                },
                onPlaylistSelected = { targetPlaylist ->
                    scope.launch {
                        val sourceName = selectedPlaylistForMerge!!.name
                        val targetName = targetPlaylist.name
                        
                        playlistsViewModel.mergePlaylists(
                            selectedPlaylistForMerge!!.id,
                            targetPlaylist.id
                        )
                        
                        android.widget.Toast.makeText(
                            context,
                            context.getString(R.string.playlist_merged, sourceName, targetName),
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                        
                        showMergeBottomSheet = false
                        selectedPlaylistForMerge = null
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistListItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            if (playlist.coverImageUri != null) {
                AsyncImage(
                    model = Uri.parse(playlist.coverImageUri),
                    contentDescription = playlist.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_playlist),
                        contentDescription = playlist.name,
                        tint = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Text(
                text = getTracksCountText(playlist.tracks.size),
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}

@Composable
private fun getTracksCountText(count: Int): String {
    val remainder10 = count % 10
    val remainder100 = count % 100
    
    return when {
        remainder10 == 1 && remainder100 != 11 -> stringResource(R.string.tracks_count_one, count)
        remainder10 in 2..4 && remainder100 !in 12..14 -> stringResource(R.string.tracks_count_few, count)
        else -> stringResource(R.string.tracks_count_many, count)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MergePlaylistBottomSheet(
    sourcePlaylist: Playlist,
    allPlaylists: List<Playlist>,
    onDismiss: () -> Unit,
    onPlaylistSelected: (Playlist) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
            
            if (allPlaylists.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_playlists_to_merge),
                    fontSize = 14.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn {
                    items(allPlaylists) { playlist ->
                        PlaylistSelectionItem(
                            playlist = playlist,
                            onClick = {
                                onPlaylistSelected(playlist)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

