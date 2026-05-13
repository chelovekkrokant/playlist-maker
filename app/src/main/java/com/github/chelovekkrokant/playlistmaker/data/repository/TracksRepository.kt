package com.github.chelovekkrokant.playlistmaker.data.repository

import com.github.chelovekkrokant.playlistmaker.domain.SearchResult

interface TracksRepository {
    suspend fun searchTracks(query: String): SearchResult
}
