package com.practicum.playlistmaker.presentation

class TrackPlayerImpl : TrackPlayer {
    private var currentObserver: TrackPlayer.StatusObserver? = null
    private var currentTrackId: String? = null
    private var isPlaying = false

    override fun play(trackId: String, statusObserver: TrackPlayer.StatusObserver) {
        currentTrackId = trackId
        currentObserver = statusObserver
        isPlaying = true
        statusObserver.onPlay()
        
        // Имитация прогресса воспроизведения
        // В реальном приложении здесь будет логика медиаплеера
    }



    override fun pause(trackId: String) {
        if (currentTrackId == trackId && isPlaying) {
            isPlaying = false
            currentObserver?.onStop()
        }
    }

    override fun seek(trackId: String, position: Float) {
        if (currentTrackId == trackId) {
            // Имитация перемотки
            currentObserver?.onProgress(position)
        }
    }

    override fun release(trackId: String) {
        if (currentTrackId == trackId) {
            currentObserver = null
            currentTrackId = null
            isPlaying = false
        }
    }
}

