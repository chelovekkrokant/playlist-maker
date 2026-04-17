package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.creator.Storage
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackDto
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.delay

class TrackRepositoryImpl(
    private val storage: Storage
) : TrackRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        delay(1000) // Эммулируем задержку ответа
        return storage.search(expression).map { it.toTrack() }
    }

    override suspend fun getAllTracks(): List<Track> {
        delay(1000) // Эммулируем задержку ответа
        return storage.search("").map { it.toTrack() }
    }

    private fun TrackDto.toTrack(): Track {
        val seconds = trackTimeMillis / 1000
        val minutes = seconds / 60
        val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
        return Track(
            id = id,
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            image = "",
            favorite = false,
            playlistId = 0L
        )
    }
}