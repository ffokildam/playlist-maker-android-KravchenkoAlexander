package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.presentation.TrackModel

class TrackSearchInteractorImpl(private val repository: TracksRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }

    override fun loadTrackData(
        trackId: String,
        onComplete: (TrackModel) -> Unit
    ) {
        // Имитация загрузки данных трека
        // В реальном приложении здесь будет запрос к репозиторию
        // Для примера создаём заглушку
        val trackModel = TrackModel(
            id = trackId,
            name = "Track Name",
            author = "Artist Name",
            pictureUrl = "https://example.com/cover.jpg"
        )
        onComplete(trackModel)
    }
}

