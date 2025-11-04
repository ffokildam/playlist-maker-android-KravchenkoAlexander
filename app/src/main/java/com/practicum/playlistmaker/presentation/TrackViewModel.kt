package com.practicum.playlistmaker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.PlaylistMakerApplication
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrackViewModel(
    private val trackId: String,
    private val tracksInteractor: TrackSearchInteractor,
    private val trackPlayer: TrackPlayer,
) : ViewModel() {

    private val _trackScreenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading)
    val trackScreenState: StateFlow<TrackScreenState> = _trackScreenState.asStateFlow()

    private val _playerStatusState = MutableStateFlow(PlayerStatus.Initial)
    val playerStatusState: StateFlow<PlayerStatus> = _playerStatusState.asStateFlow()

    init {
        tracksInteractor.loadTrackData(
            trackId = trackId,
            onComplete = { trackModel ->
                // 1
                _trackScreenState.value = TrackScreenState.Content(trackModel)
            }
        )
    }

    fun play() {
        trackPlayer.play(
            trackId = trackId,
            // 1
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onProgress(progress: Float) {
                    // 2
                    _playerStatusState.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onStop() {
                    // 3
                    _playerStatusState.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                    // 4
                    _playerStatusState.value = getCurrentPlayStatus().copy(isPlaying = true)
                }
            },
        )
    }

    // 5
    fun pause() {
        trackPlayer.pause(trackId)
    }

    fun seek(position: Float) {
        trackPlayer.seek(trackId, position)
    }

    // 6
    override fun onCleared() {
        trackPlayer.release(trackId)
    }

    private fun getCurrentPlayStatus(): PlayerStatus {
        return _playerStatusState.value
    }

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myApp = this[APPLICATION_KEY] as PlaylistMakerApplication
                val interactor = myApp.provideTrackSearchInteractor()
                val trackPlayer = myApp.provideTrackPlayer()

                TrackViewModel(
                    trackId,
                    interactor,
                    trackPlayer,
                )
            }
        }
    }
}

