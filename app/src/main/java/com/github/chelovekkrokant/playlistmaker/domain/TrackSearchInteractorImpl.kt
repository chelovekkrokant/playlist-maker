package com.github.chelovekkrokant.playlistmaker.domain

import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor
import com.github.chelovekkrokant.playlistmaker.domain.Track

class TrackSearchInteractorImpl(private val repository: TrackRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }
}