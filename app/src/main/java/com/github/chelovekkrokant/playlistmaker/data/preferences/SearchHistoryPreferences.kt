package com.github.chelovekkrokant.playlistmaker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history-preferences") + SupervisorJob())
) {

    private val preferencesKey = stringPreferencesKey("history")

    fun addEntry(word: String) {
        if (word.isEmpty()) return
        coroutineScope.launch {
            dataStore.updateData { preferences ->
                val historyString = preferences[preferencesKey].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }
                history.remove(word)
                history.add(0, word)
                val subList = history.subList(0, minOf(history.size, MAX_ENTRIES))
                preferences.toMutablePreferences().apply {
                    this[preferencesKey] = subList.joinToString(SEPARATOR)
                }
            }
        }
    }

    suspend fun getEntries(): List<String> {
        val historyString = dataStore.data.first()[preferencesKey].orEmpty()
        return if (historyString.isNotEmpty()) historyString.split(SEPARATOR) else emptyList()
    }

    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
    }
}
