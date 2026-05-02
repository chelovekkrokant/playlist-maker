package com.github.chelovekkrokant.playlistmaker.data.db

import com.github.chelovekkrokant.playlistmaker.domain.api.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope

class SearchHistoryRepositoryImpl(private val scope: CoroutineScope) : SearchHistoryRepository {
    private val database = DatabaseMock

    override fun getHistoryRequests(): List<String> {
        return database.getHistoryRequests()
    }

    override fun addToHistory(word: String) {
        database.addToHistory(word = word)
    }
}
