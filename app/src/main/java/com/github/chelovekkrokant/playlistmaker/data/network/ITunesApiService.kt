package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") query: String,
        @Query("entity") entity: String = "song"
    ): TracksSearchResponse
}