package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.ui.presentation.TrackPlayer
import com.practicum.playlistmaker.ui.presentation.TrackPlayerImpl
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    private fun getTracksRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(iTunesApiService), scope)
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