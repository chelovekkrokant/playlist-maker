package com.github.chelovekkrokant.playlistmaker.data.db

import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private fun millisToTime(ms: Long): String {
    val s = ms / 1000; val m = s / 60
    return "%02d:%02d".format(m, s - m * 60)
}

object DatabaseMock {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>(
        Track(1, "Владивосток 2000", "Мумий Троль", millisToTime(158000), "", false, 0),
        Track(2, "Группа крови", "Кино", millisToTime(283000), "", false, 0),
        Track(3, "Не смотри назад", "Ария", millisToTime(312000), "", false, 0),
        Track(4, "Звезда по имени Солнце", "Кино", millisToTime(225000), "", false, 0),
        Track(5, "Лондон", "Аквариум", millisToTime(272000), "", false, 0),
        Track(6, "На заре", "Альянс", millisToTime(230000), "", false, 0),
        Track(7, "Перемен", "Кино", millisToTime(296000), "", false, 0),
        Track(8, "Розовый фламинго", "Сплин", millisToTime(195000), "", false, 0),
        Track(9, "Танцевать", "Мельница", millisToTime(222000), "", false, 0),
        Track(10, "Чёрный бумер", "Серега", millisToTime(241000), "", false, 0),
    )

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

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val filteredPlaylists = mutableListOf<Playlist>()
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            filteredPlaylists.add(playlist.copy(tracks = playlistTracks))
        }
        emit(filteredPlaylists.toList())
        delay(100)
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        val playlist = playlists.find { it.id == id }
        val playlistTracks = tracks.filter { it.playlistId == id }
        emit(playlist?.copy(tracks = playlistTracks))
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        val favorites = tracks.filter { it.favorite }
        emit(favorites)
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
    }

    fun searchTracks(expression: String): List<Track> {
        return tracks.filter {
            it.trackName.contains(expression, ignoreCase = true) ||
            it.artistName.contains(expression, ignoreCase = true)
        }
    }
}
