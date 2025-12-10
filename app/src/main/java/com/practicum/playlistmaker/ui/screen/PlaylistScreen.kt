package com.practicum.playlistmaker.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.ui.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.data.util.ImageStorage
import kotlinx.coroutines.launch

@Composable
fun PlaylistScreen(
    playlistId: Long,
    playlistsViewModel: PlaylistsViewModel,
    navigateBack: () -> Unit,
    onTrackClick: (Track) -> Unit = {}
) {
    val playlistFlow = playlistsViewModel.getPlaylist(playlistId)
    val playlist by playlistFlow.collectAsState(initial = null)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    playlist?.let { currentPlaylist ->
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
                            .size(24.dp)
                            .clickable { navigateBack() },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                item {
                    PlaylistInfo(
                        name = currentPlaylist.name,
                        description = currentPlaylist.description,
                        coverImageUri = currentPlaylist.coverImageUri,
                        tracks = currentPlaylist.tracks,
                        onMenuClick = { showBottomSheet = true }
                    )
                }
                
                items(currentPlaylist.tracks) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onClick = { onTrackClick(track) }
                    )
                }
            }
        }
        
        if (showBottomSheet) {
            PlaylistMenuBottomSheet(
                playlist = currentPlaylist,
                onDismiss = { showBottomSheet = false },
                onShare = {
                    showBottomSheet = false
                },
                onEdit = {
                    showBottomSheet = false
                    showEditDialog = true
                },
                onDelete = {
                    scope.launch {
                        playlistsViewModel.deletePlaylistById(currentPlaylist.id)
                        currentPlaylist.coverImageUri?.let { uri ->
                            val filePath = if (uri.startsWith("file://")) {
                                uri.removePrefix("file://")
                            } else {
                                uri
                            }
                            ImageStorage.deleteImage(context, filePath)
                        }
                        navigateBack()
                    }
                }
            )
        }
        
        if (showEditDialog) {
            EditPlaylistDialog(
                playlist = currentPlaylist,
                playlistsViewModel = playlistsViewModel,
                onDismiss = { showEditDialog = false },
                onSave = { showEditDialog = false }
            )
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Плейлист не найден")
        }
    }
}

@Composable
private fun PlaylistInfo(
    name: String,
    description: String,
    coverImageUri: String?,
    tracks: List<Track>,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
        ) {
            if (coverImageUri != null) {
                val imageUri = if (coverImageUri.startsWith("file://") || coverImageUri.startsWith("/")) {
                    val filePath = if (coverImageUri.startsWith("file://")) {
                        coverImageUri.removePrefix("file://")
                    } else {
                        coverImageUri
                    }
                    ImageStorage.getImageUri(LocalContext.current, filePath) ?: Uri.parse(coverImageUri)
                } else {
                    Uri.parse(coverImageUri)
                }
                AsyncImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.playlist_cover),
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
                    Image(
                        painter = painterResource(R.drawable.ic_playlist),
                        contentDescription = stringResource(R.string.add_cover),
                        colorFilter = ColorFilter.tint(Color.Gray),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val totalMinutes = calculateTotalMinutes(tracks)
                val tracksCount = tracks.size
                Text(
                    text = "$totalMinutes минут • $tracksCount треков",
                    fontSize = 14.sp,
                    color = Color(0xFF9E9E9E)
                )
                
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.menu),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMenuClick() },
                    tint = Color(0xFF9E9E9E)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PlaylistTrackItem(
    track: Track,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            if (track.artworkUrl100 != null) {
                AsyncImage(
                    model = track.artworkUrl100,
                    contentDescription = track.trackName,
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
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_playlist),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Text(
                text = track.artistName,
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E)
            )
        }
        
        Text(
            text = track.trackTime,
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color(0xFF9E9E9E),
            modifier = Modifier.size(16.dp)
        )
    }
}

private fun calculateTotalMinutes(tracks: List<Track>): Int {
    var totalSeconds = 0
    tracks.forEach { track ->
        val timeParts = track.trackTime.split(":")
        if (timeParts.size == 2) {
            val minutes = timeParts[0].toIntOrNull() ?: 0
            val seconds = timeParts[1].toIntOrNull() ?: 0
            totalSeconds += minutes * 60 + seconds
        }
    }
    return totalSeconds / 60
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistMenuBottomSheet(
    playlist: Playlist,
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(0.7f),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = playlist.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${playlist.tracks.size} треков",
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            MenuOptionItem(
                text = stringResource(R.string.share),
                onClick = onShare
            )
            
            MenuOptionItem(
                text = stringResource(R.string.edit_information),
                onClick = onEdit
            )
            
            MenuOptionItem(
                text = stringResource(R.string.delete_playlist),
                onClick = onDelete,
                textColor = Color.Red
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MenuOptionItem(
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = textColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun EditPlaylistDialog(
    playlist: Playlist,
    playlistsViewModel: PlaylistsViewModel,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var name by remember { mutableStateOf(playlist.name) }
    var description by remember { mutableStateOf(playlist.description) }
    var coverImageUri by remember { mutableStateOf<String?>(playlist.coverImageUri) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            coverImageUri = it.toString()
        }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_information)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Обложка:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                imagePickerLauncher.launch("image/*")
                            } else {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        imagePickerLauncher.launch("image/*")
                                    }
                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (coverImageUri != null) {
                        val imageUri = if (coverImageUri!!.startsWith("file://") || coverImageUri!!.startsWith("/")) {
                            val filePath = if (coverImageUri!!.startsWith("file://")) {
                                coverImageUri!!.removePrefix("file://")
                            } else {
                                coverImageUri!!
                            }
                            ImageStorage.getImageUri(context, filePath) ?: Uri.parse(coverImageUri)
                        } else {
                            Uri.parse(coverImageUri)
                        }
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_add_photo),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                    }
                }
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.playlist_name_required)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.playlist_description)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        selectedImageUri?.let { uri ->
                            playlist.coverImageUri?.let { oldUri ->
                                val oldFilePath = if (oldUri.startsWith("file://")) {
                                    oldUri.removePrefix("file://")
                                } else if (oldUri.startsWith("/")) {
                                    oldUri
                                } else {
                                    null
                                }
                                oldFilePath?.let { ImageStorage.deleteImage(context, it) }
                            }
                            
                            val savedImagePath = ImageStorage.saveImage(context, uri, playlist.id)
                            savedImagePath?.let { path ->
                                val imageUri = ImageStorage.getImageUri(context, path)?.toString()
                                playlistsViewModel.updatePlaylist(playlist.id, name, description, imageUri)
                            } ?: run {
                                playlistsViewModel.updatePlaylist(playlist.id, name, description, null)
                            }
                        } ?: run {
                            playlistsViewModel.updatePlaylist(playlist.id, name, description, playlist.coverImageUri)
                        }
                        onSave()
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

