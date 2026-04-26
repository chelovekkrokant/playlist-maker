package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchRequest
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchResponse
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepository
import com.github.chelovekkrokant.playlistmaker.domain.SearchResult
import com.github.chelovekkrokant.playlistmaker.domain.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(query: String): SearchResult {
        val response = networkClient.doRequest(TracksSearchRequest(query))
        return when {
            response.resultCode == -1 -> SearchResult.NetworkError
            response.resultCode == 200 && response is TracksSearchResponse ->
                SearchResult.Success(response.results.map { dto ->
                    Track(
                        id = 0L,
                        trackName = dto.trackName ?: "",
                        artistName = dto.artistName ?: "",
                        trackTime = formatMillis(dto.trackTimeMillis ?: 0L),
                        image = dto.artworkUrl100 ?: "",
                        favorite = false,
                        playlistId = 0L
                    )
                })
            else -> SearchResult.ServerError
        }
    }

    private fun formatMillis(millis: Long): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(millis))
}
