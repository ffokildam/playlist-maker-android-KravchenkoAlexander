package com.practicum.playlistmaker.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistsViewModel() : ViewModel() {
    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(scope = viewModelScope)
    private val tracksRepository: TracksRepository = Creator.provideTracksRepository(viewModelScope)

    val playlists: Flow<List<Playlist>> = flow {
        val collectedPlaylists = mutableListOf<Playlist>()
        playlistsRepository.getAllPlaylists().collect { playlist ->
            collectedPlaylists.addAll(playlist)
            emit(collectedPlaylists.toList())
        }
    }
    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
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

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track)
            .firstOrNull()
    }

}

