package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.Track

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}


