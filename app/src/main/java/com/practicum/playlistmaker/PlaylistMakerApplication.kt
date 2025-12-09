package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.database.AppDatabase
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.ui.presentation.TrackPlayer

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initDatabase(this)
    }

    fun provideTracksRepository(): TracksRepository {
        return Creator.provideTracksRepository()
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return Creator.provideTrackSearchInteractor()
    }

    fun provideTrackPlayer(): TrackPlayer {
        return Creator.provideTrackPlayer()
    }

    fun provideAppDatabase(): AppDatabase {
        return Creator.provideAppDatabase()
    }

    fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return Creator.provideSearchHistoryRepository(this)
    }

    fun providePlaylistsRepository(): PlaylistsRepository {
        return Creator.providePlaylistsRepository()
    }
}

