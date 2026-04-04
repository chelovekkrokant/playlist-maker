package com.github.chelovekkrokant.playlistmaker.data.network

import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.dto.BaseResponse
import com.github.chelovekkrokant.playlistmaker.data.dto.TrackSearchResponse

class RetrofitNetworkClient : NetworkClient{
    override fun doRequest(dto: Any): BaseResponse {
        return TrackSearchResponse(listOf())
    }
}