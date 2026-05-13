package com.github.chelovekkrokant.playlistmaker.domain.api

import com.github.chelovekkrokant.playlistmaker.domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {
    suspend fun searchTracks(expression: String): List<Track>
    fun getTrackByNameAndArtist(track: Track): Flow<Track?>
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)
    suspend fun deleteTrackFromPlaylist(track: Track)
    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
}