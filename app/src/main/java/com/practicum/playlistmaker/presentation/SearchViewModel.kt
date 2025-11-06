package com.practicum.playlistmaker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.PlaylistMakerApplication
import com.practicum.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                if (list.isEmpty()) {
                    _searchScreenState.update { SearchState.Fail("Не удалось загрузить результаты") }
                } else {
                    _searchScreenState.update { SearchState.Success(list = list) }
                }
            } catch (_: Exception) {
                _searchScreenState.update { SearchState.Fail("Не удалось загрузить результаты") }
            }
        }
    }

    fun resetState() {
        _searchScreenState.update { SearchState.Initial }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PlaylistMakerApplication
                val repository = app.provideTracksRepository()
                SearchViewModel(repository)
            }
        }
    }
}


