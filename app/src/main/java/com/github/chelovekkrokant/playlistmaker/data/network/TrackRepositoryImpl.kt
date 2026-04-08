package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.creator.Storage
import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackDto
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackSearchRequest
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackSearchResponse
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.delay

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: Storage
) : TrackRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        delay(1000) // Эммулируем задержку ответа
        return if (response.resultCode == 200) { // успешный запрос
            (response as TrackSearchResponse).results.map { it.toTrack() }
        } else {
            emptyList()
        }
    }

    override suspend fun getAllTracks(): List<Track> {
        return storage.search("").map { it.toTrack() }
    }

    private fun TrackDto.toTrack(): Track {
        val seconds = trackTimeMillis / 1000
        val minutes = seconds / 60
        val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
        return Track(trackName, artistName, trackTime)
    }
}