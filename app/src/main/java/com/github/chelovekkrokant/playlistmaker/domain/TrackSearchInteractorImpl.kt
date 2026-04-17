package com.github.chelovekkrokant.playlistmaker.domain

import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.flow.Flow

class TrackSearchInteractorImpl(private val repository: TracksRepository) : TrackSearchInteractor {

    override suspend fun searchTracks(expression: String): List<Track> =
        repository.searchTracks(expression)

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        repository.getTrackByNameAndArtist(track)

    override fun getFavoriteTracks(): Flow<List<Track>> =
        repository.getFavoriteTracks()

    override fun deleteTracksByPlaylistId(playlistId: Long) =
        repository.deleteTracksByPlaylistId(playlistId)

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) =
        repository.insertTrackToPlaylist(track, playlistId)

    override suspend fun deleteTrackFromPlaylist(track: Track) =
        repository.deleteTrackFromPlaylist(track)

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) =
        repository.updateTrackFavoriteStatus(track, isFavorite)
}