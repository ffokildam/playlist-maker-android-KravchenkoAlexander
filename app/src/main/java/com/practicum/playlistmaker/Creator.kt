package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.presentation.TrackPlayer
import com.practicum.playlistmaker.presentation.TrackPlayerImpl

object Creator {
    private val storage = com.practicum.playlistmaker.creator.Storage()

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(storage))
    }

    fun provideTracksRepository(): TracksRepository {
        return getTracksRepository()
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }

    fun provideTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }
}

