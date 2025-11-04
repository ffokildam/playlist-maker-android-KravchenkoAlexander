package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.Track

interface TrackSearchInteractor {
    fun searchTracks(expression: String): List<Track>
}

