package com.github.chelovekkrokant.playlistmaker.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DatabaseMock(val scope: CoroutineScope) {

    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    fun getHistoryRequests(): List<String> {
        return historyList.toList()
    }

    fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun addToHistory(word: String) {
        historyList.add(word)
        notifyHistoryChanged()
    }
}
