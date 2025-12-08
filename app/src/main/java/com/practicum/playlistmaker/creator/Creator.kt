package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.ui.presentation.TrackPlayer
import com.practicum.playlistmaker.ui.presentation.TrackPlayerImpl
import kotlinx.coroutines.CoroutineScope

object Creator {
    private val storage = Storage()

    private fun getTracksRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(storage), scope)
    }

    fun provideTracksRepository(scope: CoroutineScope): TracksRepository {
        return getTracksRepository(scope)
    }

    fun provideTrackSearchInteractor(scope: CoroutineScope): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository(scope))
    }

    fun provideTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }
}