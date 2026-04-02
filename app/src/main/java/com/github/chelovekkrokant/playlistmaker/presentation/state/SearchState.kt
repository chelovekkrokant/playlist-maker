package com.github.chelovekkrokant.playlistmaker.presentation.state

import com.github.chelovekkrokant.playlistmaker.domain.model.Track

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Error(val error: String) : SearchState()
}
