package com.github.chelovekkrokant.playlistmaker.domain

sealed class SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult()
    object NetworkError : SearchResult()
    object ServerError : SearchResult()
}