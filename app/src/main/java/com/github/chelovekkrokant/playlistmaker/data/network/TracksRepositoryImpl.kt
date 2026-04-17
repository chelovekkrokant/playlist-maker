package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.db.DatabaseMock
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    //private val storage: Storage
) : TracksRepository {
    private val database = DatabaseMock(
        scope = scope
    )

//    suspend fun searchTracksOld(expression: String): List<Track> {
//        delay(1000) // Эммулируем задержку ответа
//        return storage.search(expression).map { it.toTrack() }
//    }
//    suspend fun getAllTracks(): List<Track> {
//        delay(1000) // Эммулируем задержку ответа
//        return storage.search("").map { it.toTrack() }
//    }
//
//    private fun TrackDto.toTrack(): Track {
//        val seconds = trackTimeMillis / 1000
//        val minutes = seconds / 60
//        val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
//        return Track(
//            id = id,
//            trackName = trackName,
//            artistName = artistName,
//            trackTime = trackTime,
//            image = "",
//            favorite = false,
//            playlistId = 0L
//        )
//    }

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