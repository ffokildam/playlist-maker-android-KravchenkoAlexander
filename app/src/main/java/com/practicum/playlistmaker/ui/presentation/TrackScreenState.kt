package com.practicum.playlistmaker.ui.presentation

sealed class TrackScreenState {
    object Loading : TrackScreenState()
    data class Content(
        val trackModel: TrackModel,
    ) : TrackScreenState()
}

