package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.ui.presentation.TrackPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PlaylistMakerApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    fun provideTracksRepository(): TracksRepository {
        return Creator.provideTracksRepository(applicationScope)
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return Creator.provideTrackSearchInteractor(applicationScope)
    }

    fun provideTrackPlayer(): TrackPlayer {
        return Creator.provideTrackPlayer()
    }
}

