package com.github.chelovekkrokant.playlistmaker.creator

import com.github.chelovekkrokant.playlistmaker.data.network.TracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.domain.TrackSearchInteractorImpl
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.CoroutineScope

object Creator {
    fun getTrackRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(scope)
    }

    fun provideTrackSearchInteractor(scope: CoroutineScope): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackRepository(scope))
    }
}