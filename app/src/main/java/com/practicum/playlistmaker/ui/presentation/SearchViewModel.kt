package com.practicum.playlistmaker.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.PlaylistMakerApplication
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchHistory.update { searchHistoryRepository.getHistory() }
        }
    }

    fun search(whatSearch: String) {
        if (whatSearch.isNotEmpty()) {
            searchHistoryRepository.addToHistory(whatSearch)
            loadHistory()
        }
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
        loadHistory()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PlaylistMakerApplication
                val tracksRepository = app.provideTracksRepository()
                val searchHistoryRepository = app.provideSearchHistoryRepository()
                SearchViewModel(tracksRepository, searchHistoryRepository)
            }
        }
    }
}


