package com.github.chelovekkrokant.playlistmaker.data.database.mapper

import com.github.chelovekkrokant.playlistmaker.data.database.entity.TrackEntity
import com.github.chelovekkrokant.playlistmaker.domain.Track

fun TrackEntity.toTrack(): Track = Track(
    id = id,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    image = image,
    favorite = favorite,
    playlistId = playlistId
)

fun Track.toEntity(): TrackEntity = TrackEntity(
    id = id,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    image = image,
    favorite = favorite,
    playlistId = playlistId
)
