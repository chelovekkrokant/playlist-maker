package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.creator.Creator
import com.github.chelovekkrokant.playlistmaker.data.db.SearchHistoryRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.network.TrackRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val trackRepository : TrackRepository
) : ViewModel() {
    private val searchHistoryRepository = SearchHistoryRepositoryImpl(scope = viewModelScope)
    private val _searchQuery = MutableStateFlow("")
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        performSearch(query)
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                searchHistoryRepository.addToHistory(request)
                val list = trackRepository.searchTracks(expression = request)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException) {
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.update { SearchState.Initial }
    }

    suspend fun getHistoryList() = searchHistoryRepository.getHistoryRequests()

}

class SearchViewModelUnrelevant(
    private val tracksRepository: TrackRepository
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()

    fun search(whatSearch: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException){
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
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