package com.practicum.playlistmaker.presentation

sealed class TrackScreenState {
    object Loading : TrackScreenState()
    data class Content(
        val trackModel: TrackModel,
    ) : TrackScreenState()
}

