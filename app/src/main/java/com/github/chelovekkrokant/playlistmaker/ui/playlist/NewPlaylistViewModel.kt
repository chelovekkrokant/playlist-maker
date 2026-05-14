package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _coverImageUri = MutableStateFlow<String?>(null)
    val coverImageUri: StateFlow<String?> = _coverImageUri.asStateFlow()

    fun setCoverImageUri(uri: String?) {
        _coverImageUri.value = uri
    }

    fun createNewPlaylist(name: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(name, description, _coverImageUri.value)
        }
    }
}
