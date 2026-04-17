package com.github.chelovekkrokant.playlistmaker.domain.api

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun getHistoryRequests(): Flow<List<String>>

    fun addToHistory(word: String)
}