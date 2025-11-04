package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}

