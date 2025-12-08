package com.practicum.playlistmaker.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val playlistId: Long = 0,
    val favorite: Boolean = false
) : Parcelable

