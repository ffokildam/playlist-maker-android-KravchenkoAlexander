package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.creator.DatabaseMock
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.CoroutineScope

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope
) : PlaylistsRepository {
    private val database = DatabaseMock(
        scope = scope,
    )

    override fun getPlaylist(playlistId: Long) = database.getPlaylist(playlistId)

    override fun getAllPlaylists() = database.getAllPlaylists()

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(
            name = name,
            description = description
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(playlistId = id)
    }
}

