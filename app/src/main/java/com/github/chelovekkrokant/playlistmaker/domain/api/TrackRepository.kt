package com.github.chelovekkrokant.playlistmaker.domain.api

import com.github.chelovekkrokant.playlistmaker.data.network.Track

interface TrackRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun getAllTracks(): List<Track>
}