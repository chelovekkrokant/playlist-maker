package com.github.chelovekkrokant.playlistmaker.data

import com.github.chelovekkrokant.playlistmaker.data.dto.TrackSearchRequest
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackSearchResponse
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) { // успешный запрос
            return (response as TrackSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
                Track(it.trackName, it.artistName, trackTime) }
        } else {
            return emptyList()
        }
    }
}