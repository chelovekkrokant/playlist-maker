package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.creator.Creator
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val TrackRepository: TrackRepository,
) : ViewModel() {

    //...
    private val _allTracksScreenState = MutableStateFlow<SearchState>(SearchState.Loading)
    val allTracksScreenState = _allTracksScreenState.asStateFlow()

    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allTracksScreenState.update { SearchState.Loading }
                val list = TrackRepository.getAllTracks()
                _allTracksScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException){
                _allTracksScreenState.update { SearchState.Error(e.message.toString()) }
            }
        }
    }


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getTrackRepository()) as T
                }
            }
    }
}