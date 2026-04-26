package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.dto.BaseResponse
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchRequest
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchResponse
import java.io.IOException

class RetrofitNetworkClient(private val api: ITunesApiService) : NetworkClient {
    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> api.searchTracks(dto.expression).apply { resultCode = 200 }
                else -> BaseResponse().apply { resultCode = 400 }
            }
        } catch (e: IOException) {
            BaseResponse().apply { resultCode = -1 }
        }
    }
}