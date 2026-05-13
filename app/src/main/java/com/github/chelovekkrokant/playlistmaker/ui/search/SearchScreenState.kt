package com.github.chelovekkrokant.playlistmaker.ui.search

import com.github.chelovekkrokant.playlistmaker.domain.Track

sealed class SearchScreenState {
    object Empty : SearchScreenState()
    object Loading : SearchScreenState()
    object NothingFound : SearchScreenState()
    object Error : SearchScreenState()
    data class Content(val tracks: List<Track>) : SearchScreenState()
}
