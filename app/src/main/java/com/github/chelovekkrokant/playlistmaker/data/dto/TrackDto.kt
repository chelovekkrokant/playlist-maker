package com.github.chelovekkrokant.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?
)