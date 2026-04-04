package com.github.chelovekkrokant.playlistmaker.domain

import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor

class TrackSearchInteractorImpl(private val repository: TrackRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }
    override suspend fun getAllTracks(): List<Track> {
        return repository.getAllTracks()
    }
}