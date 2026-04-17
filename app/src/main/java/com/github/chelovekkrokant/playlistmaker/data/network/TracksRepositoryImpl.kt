package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.db.DatabaseMock
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class TracksRepositoryImpl(
    private val scope: CoroutineScope,
) : TracksRepository {
    private val database = DatabaseMock(scope = scope)

    override suspend fun searchTracks(expression: String): List<Track> {
        return database.searchTracks(expression)
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track.copy(playlistId = playlistId))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.insertTrack(track.copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }
}