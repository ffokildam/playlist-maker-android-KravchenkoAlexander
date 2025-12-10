package com.practicum.playlistmaker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val playlistId: Long = 0,
    val favorite: Boolean = false
)

