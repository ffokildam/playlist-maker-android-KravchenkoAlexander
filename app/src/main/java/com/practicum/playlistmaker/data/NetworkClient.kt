package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.BaseResponse



// по заданию нужно было добавить
interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}

