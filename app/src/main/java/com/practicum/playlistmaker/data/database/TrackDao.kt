package com.practicum.playlistmaker.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE trackName = :trackName AND artistName = :artistName")
    fun getTrackByNameAndArtist(trackName: String, artistName: String): Flow<TrackEntity?>

    @Query("SELECT * FROM tracks WHERE favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE playlistId = :playlistId")
    fun getTracksByPlaylistId(playlistId: Long): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM tracks WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Long)

    @Query("UPDATE tracks SET playlistId = 0 WHERE id = :trackId AND playlistId != 0")
    suspend fun removeTrackFromPlaylist(trackId: Long)
    
    @Query("UPDATE tracks SET playlistId = :toPlaylistId WHERE playlistId = :fromPlaylistId")
    suspend fun moveTracksToPlaylist(fromPlaylistId: Long, toPlaylistId: Long)
}

