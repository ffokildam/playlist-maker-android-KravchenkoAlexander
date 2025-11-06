package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse

import com.practicum.playlistmaker.creator.Storage
import com.practicum.playlistmaker.data.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(dto: Any): TracksSearchResponse {
        val searchList = storage.search((dto as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}

