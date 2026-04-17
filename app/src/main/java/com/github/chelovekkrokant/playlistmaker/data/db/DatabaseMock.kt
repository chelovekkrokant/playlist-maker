package com.github.chelovekkrokant.playlistmaker.data.db

import com.github.chelovekkrokant.playlistmaker.domain.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DatabaseMock(val scope: CoroutineScope) {

    private val historyList = ()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    fun getHistoryRequests(): List<Word> {
        return historyList.toList()
    }
    fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }
    fun addToHistory(word: Word) {
        historyList.add(word)
        notifyHistoryChanged()
    }
}