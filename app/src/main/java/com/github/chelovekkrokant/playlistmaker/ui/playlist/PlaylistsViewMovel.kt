package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.data.db.DatabaseMock
import com.github.chelovekkrokant.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistsViewModel() : ViewModel() {
    var selectedTrack: Track? = null
    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(scope = viewModelScope)
    private val databaseRepository: DatabaseMock = DatabaseMock

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
        databaseRepository.insertTrack(track.copy(playlistId = playlistId))
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        databaseRepository.insertTrack(track.copy(favorite = isFavorite))
    }

    suspend fun deleteTrackFromPlaylist(track: Track) {
        databaseRepository.insertTrack(track.copy(playlistId = 0))
    }

    suspend fun deletePlaylistById(id: Long) {
        databaseRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }

    suspend fun isExist(track: Track): Track? {
        return databaseRepository.getTrackByNameAndArtist(track = track).firstOrNull()
    }
}
