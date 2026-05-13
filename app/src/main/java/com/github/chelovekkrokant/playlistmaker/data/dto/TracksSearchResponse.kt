package com.github.chelovekkrokant.playlistmaker.data.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : BaseResponse()