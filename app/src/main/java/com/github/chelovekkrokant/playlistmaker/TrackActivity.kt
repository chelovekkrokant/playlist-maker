package com.github.chelovekkrokant.playlistmaker

import androidx.activity.ComponentActivity
import androidx.activity.viewModels

class TrackActivity : ComponentActivity() {
    private val viewModel: TrackViewModel by viewModels<TrackViewModel> {
        TrackViewModel.getViewModelFactory("123")
    }
}