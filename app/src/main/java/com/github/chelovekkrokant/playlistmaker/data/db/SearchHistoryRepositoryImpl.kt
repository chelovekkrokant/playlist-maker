package com.github.chelovekkrokant.playlistmaker.data.db

import com.github.chelovekkrokant.playlistmaker.data.preferences.SearchHistoryPreferences
import com.github.chelovekkrokant.playlistmaker.domain.api.SearchHistoryRepository
import kotlinx.coroutines.runBlocking

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override fun getHistoryRequests(): List<String> = runBlocking {
        preferences.getEntries()
    }

    override fun addToHistory(word: String) {
        preferences.addEntry(word)
    }
}
