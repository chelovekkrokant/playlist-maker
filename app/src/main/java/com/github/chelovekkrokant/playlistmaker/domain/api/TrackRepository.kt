package com.github.chelovekkrokant.playlistmaker.domain.api

import com.github.chelovekkrokant.playlistmaker.domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTracks(expression: String): List<Track>
    //suspend fun getAllTracks(): List<Track>

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    fun deleteTracksByPlaylistId(playlistId: Long)

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

}