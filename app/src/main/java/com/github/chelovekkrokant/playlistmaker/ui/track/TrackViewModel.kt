package com.github.chelovekkrokant.playlistmaker.ui.track

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.chelovekkrokant.playlistmaker.creator.Creator
import com.github.chelovekkrokant.playlistmaker.domain.api.TrackSearchInteractor

class TrackViewModel(
    private val trackId: String,
    private val tracksInteractor: TrackSearchInteractor,
) : ViewModel() {

    init {
        Log.d("TEST", "init!: $trackId")
    }

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackViewModel(
                        trackId,
                        Creator.provideTrackSearchInteractor(),
                    ) as T
                }
            }
    }
}