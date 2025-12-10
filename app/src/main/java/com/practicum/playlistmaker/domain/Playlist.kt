package com.practicum.playlistmaker.domain

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImageUri: String? = null,  // URI обложки плейлиста
    var tracks: List<Track>
)

