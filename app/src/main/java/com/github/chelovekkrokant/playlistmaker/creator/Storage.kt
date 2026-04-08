package com.github.chelovekkrokant.playlistmaker.creator

import com.github.chelovekkrokant.playlistmaker.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            trackName = "Владивосток 2000",
            artistName = "Мумий Троль",
            trackTimeMillis = 158000 // 2:38
        ),
        TrackDto(
            trackName = "Группа крови",
            artistName = "Кино",
            trackTimeMillis = 283000 // 4:43
        ),
        TrackDto(
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTimeMillis = 312000 // 5:12
        ),
        TrackDto(
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTimeMillis = 225000
        ),
        TrackDto(
            trackName = "Лондон",
            artistName = "Аквариум",
            trackTimeMillis = 272000
        ),
        TrackDto(
            trackName = "На заре",
            artistName = "Альянс",
            trackTimeMillis = 230000
        ),
        TrackDto(
            trackName = "Перемен",
            artistName = "Кино",
            trackTimeMillis = 296000
        ),
        TrackDto(
            trackName = "Розовый фламинго",
            artistName = "Сплин",
            trackTimeMillis = 195000
        ),
        TrackDto(
            trackName = "Танцевать",
            artistName = "Мельница",
            trackTimeMillis = 222000
        ),
        TrackDto(
            trackName = "Чёрный бумер",
            artistName = "Серега",
            trackTimeMillis = 241000
        )
    )

    fun search(request: String): List<TrackDto> {
        val result = listTracks.filter {
            val q = request.lowercase()
            it.trackName.lowercase().contains(q) || it.artistName.lowercase().contains(q)
        }
        return result
    }
}