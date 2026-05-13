package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepository
import com.github.chelovekkrokant.playlistmaker.domain.SearchResult
import com.github.chelovekkrokant.playlistmaker.domain.api.SearchHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchState = MutableStateFlow<SearchScreenState>(SearchScreenState.Empty)

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) search(query)
                }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    private var lastQuery = ""

    fun retry() = search(lastQuery)

    fun search(query: String) {
        lastQuery = query
        viewModelScope.launch {
            searchState.value = SearchScreenState.Loading
            searchHistoryRepository.addToHistory(query)
            when (val result = tracksRepository.searchTracks(query)) {
                is SearchResult.Success ->
                    searchState.value =
                        if (result.tracks.isEmpty()) SearchScreenState.NothingFound
                        else SearchScreenState.Content(result.tracks)
                is SearchResult.NetworkError -> searchState.value = SearchScreenState.Error
                is SearchResult.ServerError -> searchState.value = SearchScreenState.Error
            }
        }
    }

    fun clearSearch() {
        searchState.value = SearchScreenState.Empty
    }

    fun getHistoryList() = searchHistoryRepository.getHistoryRequests()

    fun getFoundTracks() =
        (searchState.value as? SearchScreenState.Content)?.tracks ?: emptyList()
}