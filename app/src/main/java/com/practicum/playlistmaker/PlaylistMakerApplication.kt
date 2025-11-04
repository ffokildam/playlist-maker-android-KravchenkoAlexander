package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.presentation.TrackPlayer
import com.practicum.playlistmaker.presentation.TrackPlayerImpl

class PlaylistMakerApplication : Application() {
    fun getRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getRepository())
    }

    fun provideTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }
}

