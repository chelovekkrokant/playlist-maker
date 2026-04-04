package com.github.chelovekkrokant.playlistmaker.data

import com.github.chelovekkrokant.playlistmaker.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}