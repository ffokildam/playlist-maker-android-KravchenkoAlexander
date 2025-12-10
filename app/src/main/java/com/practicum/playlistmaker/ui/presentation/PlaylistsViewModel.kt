package com.practicum.playlistmaker.ui.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.PlaylistMakerApplication
import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as PlaylistMakerApplication
    private val playlistsRepository: PlaylistsRepository = app.providePlaylistsRepository()
    private val tracksRepository: TracksRepository = app.provideTracksRepository()

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()
    
    fun getPlaylist(playlistId: Long): Flow<Playlist?> = playlistsRepository.getPlaylist(playlistId)

    suspend fun createNewPlayList(namePlaylist: String, description: String, coverImageUri: String? = null): Long {
        return playlistsRepository.addNewPlaylist(namePlaylist, description, coverImageUri)
    }
    
    suspend fun updatePlaylist(id: Long, name: String, description: String, coverImageUri: String?) {
        playlistsRepository.updatePlaylist(id, name, description, coverImageUri)
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        tracksRepository.insertTrackToPlaylist(track, playlistId)
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
    }

    suspend fun deleteTrackFromPlaylist(track: Track) {
        tracksRepository.deleteTrackFromPlaylist(track)
    }

    suspend fun deletePlaylistById(id: Long) {
        tracksRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }
    
    suspend fun mergePlaylists(fromPlaylistId: Long, toPlaylistId: Long) {
        playlistsRepository.mergePlaylists(fromPlaylistId, toPlaylistId)
    }

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track)
            .firstOrNull()
    }

}

