package com.github.chelovekkrokant.playlistmaker

import com.github.chelovekkrokant.playlistmaker.data.TrackRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.network.RetrofitNetworkClient
import com.github.chelovekkrokant.playlistmaker.domain.TrackSearchInteractorImpl
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }
}