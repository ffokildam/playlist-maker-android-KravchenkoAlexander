package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.database.AppDatabase
import com.practicum.playlistmaker.data.database.PlaylistEntity
import com.practicum.playlistmaker.data.database.toDomain
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    database: AppDatabase
) : PlaylistsRepository {
    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<com.practicum.playlistmaker.domain.Playlist?> {
        return combine(
            playlistDao.getPlaylistById(playlistId),
            trackDao.getTracksByPlaylistId(playlistId)
        ) { playlistEntity, trackEntities ->
            playlistEntity?.toDomain(trackEntities.map { it.toDomain() })
        }
    }

    override fun getAllPlaylists(): Flow<List<com.practicum.playlistmaker.domain.Playlist>> {
        return combine(
            playlistDao.getAllPlaylists(),
            trackDao.getAllTracks()
        ) { playlistEntities, allTracks ->
            playlistEntities.map { playlistEntity ->
                val playlistTracks = allTracks
                    .filter { it.playlistId == playlistEntity.id }
                    .map { it.toDomain() }
                playlistEntity.toDomain(playlistTracks)
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description
            )
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistDao.deletePlaylistById(id)
        trackDao.deleteTracksByPlaylistId(id)
    }
}

