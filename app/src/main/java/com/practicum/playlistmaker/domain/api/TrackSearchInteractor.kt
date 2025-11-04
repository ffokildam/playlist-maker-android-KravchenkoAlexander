package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.presentation.TrackModel

interface TrackSearchInteractor {
    fun searchTracks(expression: String): List<Track>
    
    fun loadTrackData(
        trackId: String,
        onComplete: (TrackModel) -> Unit
    )
}

