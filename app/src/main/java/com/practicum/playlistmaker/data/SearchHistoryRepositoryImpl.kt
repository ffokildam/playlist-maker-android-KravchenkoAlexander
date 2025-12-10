package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.preferences.SearchHistoryPreferences
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<String> {
        return searchHistoryPreferences.getEntries()
    }

    override fun addToHistory(word: String) {
        searchHistoryPreferences.addEntry(word)
    }
}

