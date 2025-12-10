package com.practicum.playlistmaker.data.database

import com.practicum.playlistmaker.domain.Playlist
import com.practicum.playlistmaker.domain.Track

fun PlaylistEntity.toDomain(tracks: List<Track> = emptyList()): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        coverImageUri = this.coverImageUri,
        tracks = tracks
    )
}

fun TrackEntity.toDomain(): Track {
    return Track(
        id = this.id,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        artworkUrl100 = this.artworkUrl100,
        previewUrl = this.previewUrl,
        playlistId = this.playlistId,
        favorite = this.favorite
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = this.id,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        artworkUrl100 = this.artworkUrl100,
        previewUrl = this.previewUrl,
        playlistId = this.playlistId,
        favorite = this.favorite
    )
}

