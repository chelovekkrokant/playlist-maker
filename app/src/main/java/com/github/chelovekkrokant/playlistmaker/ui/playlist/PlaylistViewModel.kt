package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistViewModel(
    private val playlistId: Long
) : ViewModel() {
    private val repository: PlaylistsRepository = PlaylistsRepositoryImpl(scope = viewModelScope)
    val playlist: Flow<Playlist?> = repository.getPlaylist(playlistId)

    companion object {
        fun factory(playlistId: Long) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PlaylistViewModel(playlistId) as T
            }
        }
    }
}
