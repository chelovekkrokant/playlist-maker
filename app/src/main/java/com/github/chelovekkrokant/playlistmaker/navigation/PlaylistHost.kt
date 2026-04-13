package com.github.chelovekkrokant.playlistmaker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.chelovekkrokant.playlistmaker.ui.main.MainScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistScreen
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
                onPlaylistsClick = { navController.navigate(Screen.Playlist.name) }
            )
        }
        composable(Screen.Search.name) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                modifier = Modifier,
                viewModel = searchViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Playlist.name) {
            PlaylistScreen()
        }
    }
}
