package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.database.AppDatabase
import com.github.chelovekkrokant.playlistmaker.data.database.entity.PlaylistEntity
import com.github.chelovekkrokant.playlistmaker.data.database.mapper.toPlaylist
import com.github.chelovekkrokant.playlistmaker.data.database.mapper.toTrack
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PlaylistsRepositoryImpl(database: AppDatabase) : PlaylistsRepository {

    private val playlistsDao = database.playlistsDao()
    private val tracksDao = database.tracksDao()

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return combine(playlistsDao.getAllPlaylists(), tracksDao.getAllTracks()) { entities, tracks ->
            entities.map { entity ->
                val playlistTracks = tracks.filter { it.playlistId == entity.id }.map { it.toTrack() }
                entity.toPlaylist(playlistTracks)
            }
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(
            playlistsDao.getPlaylistById(playlistId),
            tracksDao.getTracksByPlaylistId(playlistId)
        ) { entity, tracks ->
            entity?.toPlaylist(tracks.map { it.toTrack() })
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        playlistsDao.insertPlaylist(PlaylistEntity(name = name, description = description))
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistsDao.deletePlaylistById(id)
    }
}
