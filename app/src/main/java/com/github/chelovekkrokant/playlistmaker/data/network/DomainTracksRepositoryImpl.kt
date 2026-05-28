package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.database.AppDatabase
import com.github.chelovekkrokant.playlistmaker.data.database.mapper.toEntity
import com.github.chelovekkrokant.playlistmaker.data.database.mapper.toTrack
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DomainTracksRepositoryImpl(database: AppDatabase) : TracksRepository {

    private val dao = database.tracksDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        return dao.getAllTracks().first().filter {
            it.trackName.contains(expression, ignoreCase = true) ||
                    it.artistName.contains(expression, ignoreCase = true)
        }.map { it.toTrack() }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return dao.getTrackByNameAndArtist(track.trackName, track.artistName).map { it?.toTrack() }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getFavoriteTracks().map { list -> list.map { it.toTrack() } }
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        dao.deleteTracksByPlaylistId(playlistId)
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        val existing = dao.getTrackByNameAndArtist(track.trackName, track.artistName).first()
        val isFavorite = existing?.favorite ?: track.favorite
        dao.insertTrack(track.copy(playlistId = playlistId, favorite = isFavorite).toEntity())
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        dao.insertTrack(track.copy(playlistId = 0).toEntity())
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        dao.insertTrack(track.copy(favorite = isFavorite).toEntity())
    }
}
