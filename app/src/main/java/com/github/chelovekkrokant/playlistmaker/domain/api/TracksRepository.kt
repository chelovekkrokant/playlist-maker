package com.github.chelovekkrokant.playlistmaker.domain.api

import com.github.chelovekkrokant.playlistmaker.domain.model.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}
