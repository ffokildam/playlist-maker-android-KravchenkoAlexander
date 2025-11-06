package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.presentation.TrackPlayer

class PlaylistMakerApplication : Application() {
    fun provideTracksRepository(): TracksRepository {
        return Creator.provideTracksRepository()
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return Creator.provideTrackSearchInteractor()
    }

    fun provideTrackPlayer(): TrackPlayer {
        return Creator.provideTrackPlayer()
    }
}

