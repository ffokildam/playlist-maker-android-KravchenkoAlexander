package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>
    
    fun getAllPlaylists(): Flow<List<Playlist>>
    
    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null): Long
    
    suspend fun updatePlaylist(id: Long, name: String, description: String, coverImageUri: String?)
    
    suspend fun deletePlaylistById(id: Long)
    
    suspend fun mergePlaylists(fromPlaylistId: Long, toPlaylistId: Long)
}

