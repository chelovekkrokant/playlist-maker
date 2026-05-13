package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.lifecycle.ViewModel
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistViewModel(
    private val playlistId: Long,
    private val repository: PlaylistsRepository
) : ViewModel() {

    val playlist: Flow<Playlist?> = repository.getPlaylist(playlistId)
}