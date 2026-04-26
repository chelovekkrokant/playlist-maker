package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chelovekkrokant.playlistmaker.creator.Creator
import com.github.chelovekkrokant.playlistmaker.data.db.SearchHistoryRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepository
import com.github.chelovekkrokant.playlistmaker.domain.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val tracksRepository: TracksRepository = Creator.getTracksRepository()
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
            _searchScreenState.update { SearchState.Searching }
            searchHistoryRepository.addToHistory(request)
            when (val result = tracksRepository.searchTracks(request)) {
                is SearchResult.Success -> _searchScreenState.update { SearchState.Success(result.tracks) }
                is SearchResult.NetworkError -> _searchScreenState.update { SearchState.Fail("Нет подключения к интернету") }
                is SearchResult.ServerError -> _searchScreenState.update { SearchState.Fail("Ошибка сервера") }
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.update { SearchState.Initial }
    }

    fun getHistoryList() = searchHistoryRepository.getHistoryRequests()

    fun getFoundTracks() =
        (_searchScreenState.value as? SearchState.Success)?.foundList ?: emptyList()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    SearchViewModel() as T
            }
    }
}
