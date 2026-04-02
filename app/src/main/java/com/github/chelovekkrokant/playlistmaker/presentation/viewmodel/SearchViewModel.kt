package com.github.chelovekkrokant.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.creator.Creator
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import com.github.chelovekkrokant.playlistmaker.presentation.state.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository,
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    fun searchTracks(expression: String) {
        if (expression.isBlank()) {
            _searchState.update { SearchState.Initial }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchState.update { SearchState.Loading }
                val list = tracksRepository.searchTracks(expression)
                _searchState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException) {
                _searchState.update { SearchState.Error(e.message.orEmpty()) }
            }
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.provideTracksRepository()) as T
                }
            }
    }
}
