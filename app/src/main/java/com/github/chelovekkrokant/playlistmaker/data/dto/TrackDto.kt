package com.github.chelovekkrokant.playlistmaker.data.dto

data class TrackDto(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
)