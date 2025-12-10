package com.practicum.playlistmaker.domain.api

interface SearchHistoryRepository {
    suspend fun getHistory(): List<String>
    fun addToHistory(word: String)
}

