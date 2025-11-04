package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}

