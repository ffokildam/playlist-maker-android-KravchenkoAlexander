package com.practicum.playlistmaker.ui.presentation

data class PlayerStatus(
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
) {
    companion object {
        val Initial = PlayerStatus()
    }
}

