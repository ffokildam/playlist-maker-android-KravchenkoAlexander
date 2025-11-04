package com.practicum.playlistmaker.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.PlaylistMakerApplication
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor

class TrackViewModel(
    private val trackId: String,
    private val tracksInteractor: TrackSearchInteractor,
) : ViewModel() {

    init {
        Log.d("TEST", "init!: $trackId")
    }

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as PlaylistMakerApplication).provideTrackSearchInteractor()

                TrackViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }
}

