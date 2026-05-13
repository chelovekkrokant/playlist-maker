package com.github.chelovekkrokant.playlistmaker.data.database.mapper

import com.github.chelovekkrokant.playlistmaker.data.database.entity.PlaylistEntity
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track

fun PlaylistEntity.toPlaylist(tracks: List<Track>): Playlist = Playlist(
    id = id,
    name = name,
    description = description,
    tracks = tracks
)

fun Playlist.toEntity(): PlaylistEntity = PlaylistEntity(
    id = id,
    name = name,
    description = description
)
