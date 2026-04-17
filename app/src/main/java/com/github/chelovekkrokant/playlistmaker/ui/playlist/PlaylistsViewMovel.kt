package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.data.db.DatabaseMock
import com.github.chelovekkrokant.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.network.TracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistsViewModel() : ViewModel() {
    var selectedTrack: Track? = null
    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(scope = viewModelScope)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(scope = viewModelScope)
    private val databaseRepository: DatabaseMock = DatabaseMock(scope = viewModelScope)

    val playlists: Flow<List<Playlist>> = flow {
        val collectedPlaylists = mutableListOf<Playlist>()
        playlistsRepository.getAllPlaylists().collect { playlist ->
            collectedPlaylists.addAll(playlist)
            emit(collectedPlaylists.toList())
        }
    }
    val favoriteList: Flow<List<Track>> = databaseRepository.getFavoriteTracks()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        tracksRepository.insertTrackToPlaylist(track, playlistId)
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
    }

    suspend fun deleteTrackFromPlaylist(track: Track) {
        tracksRepository.deleteTrackFromPlaylist(track)
    }

    suspend fun deletePlaylistById(id: Long) {
        tracksRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track = track).firstOrNull()
    }
}