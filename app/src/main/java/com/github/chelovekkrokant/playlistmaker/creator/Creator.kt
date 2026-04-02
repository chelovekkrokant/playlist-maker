package com.github.chelovekkrokant.playlistmaker.creator

import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.network.RetrofitNetworkClient
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository

object Creator {

    private fun getNetworkClient(): NetworkClient {
        return RetrofitNetworkClient(Storage())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(getNetworkClient())
    }

    fun provideTracksRepository(): TracksRepository {
        return getTracksRepository()
    }
}
