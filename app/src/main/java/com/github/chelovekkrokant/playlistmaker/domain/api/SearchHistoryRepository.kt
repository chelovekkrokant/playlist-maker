package com.github.chelovekkrokant.playlistmaker.domain.api

interface SearchHistoryRepository {

    fun getHistoryRequests(): List<String>

    fun addToHistory(word: String)
}