package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.ui.presentation.TrackModel

interface TrackSearchInteractor {
    suspend fun searchTracks(expression: String): List<Track>
    
    fun loadTrackData(
        trackId: String,
        onComplete: (TrackModel) -> Unit
    )
}

