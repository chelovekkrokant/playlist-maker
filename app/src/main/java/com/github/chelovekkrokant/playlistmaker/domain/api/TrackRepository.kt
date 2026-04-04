package com.github.chelovekkrokant.playlistmaker.domain.api

import com.github.chelovekkrokant.playlistmaker.domain.Track

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>
    suspend fun getAllTracks(): List<Track>
}