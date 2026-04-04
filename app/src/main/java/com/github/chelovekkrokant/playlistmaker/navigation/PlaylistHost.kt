package com.github.chelovekkrokant.playlistmaker.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.chelovekkrokant.playlistmaker.ui.main.MainScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.AllTracksScreen
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchScreen
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchViewModel
import com.github.chelovekkrokant.playlistmaker.ui.settings.SettingsScreen

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.Search.name) },
                onSettingsClick = { navController.navigate(Screen.Settings.name) },
                onPlaylistsClick = { navController.navigate(Screen.AllTracks.name) }
            )
        }
        composable(Screen.Search.name) {
            SearchScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.AllTracks.name) {
            val viewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.getViewModelFactory()
            )
            AllTracksScreen(
                modifier = Modifier,
                viewModel = viewModel
            )
        }
    }
}
