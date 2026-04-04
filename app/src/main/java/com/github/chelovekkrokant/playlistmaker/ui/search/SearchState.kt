package com.github.chelovekkrokant.playlistmaker.ui.search

import com.github.chelovekkrokant.playlistmaker.domain.Track

sealed class SearchState {
    object Initial: SearchState() // Cостояние экрана при первой загрузке
    object Loading: SearchState() // Cостояние экрана при начале поиска
    data class Success(val foundList: List<Track>): SearchState() // Cостояние экрана при успешном завершении поиска
    data class Error(val error: String): SearchState() // Cостояние экрана если при запросе к серверу произошла ошибка
}