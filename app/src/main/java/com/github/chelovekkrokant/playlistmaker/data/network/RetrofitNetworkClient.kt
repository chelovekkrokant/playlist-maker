package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.creator.Storage
import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.dto.BaseResponse
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchRequest
import com.github.chelovekkrokant.playlistmaker.data.dto.TracksSearchResponse

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        if (dto is TracksSearchRequest) {
            val results = storage.search(dto.expression)
            return TracksSearchResponse(results).apply { resultCode = 200 }
        }
        return BaseResponse().apply { resultCode = 400 }
    }
}
