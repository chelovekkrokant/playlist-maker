package com.github.chelovekkrokant.playlistmaker.domain

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)