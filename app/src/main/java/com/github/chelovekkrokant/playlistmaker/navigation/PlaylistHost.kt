package com.github.chelovekkrokant.playlistmaker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.chelovekkrokant.playlistmaker.ui.main.MainScreen
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchScreen
import com.github.chelovekkrokant.playlistmaker.ui.settings.SettingsScreen

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onSearchClick = { navigateToSearch(navController) },
                onSettingsClick = { navigateToSettings(navController) }
            )
        }
        composable(Screen.Search.name) {
            SearchScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

private fun navigateToSearch(navController: NavHostController) {
    navController.navigate(Screen.Search.name)
}

private fun navigateToSettings(navController: NavHostController) {
    navController.navigate(Screen.Settings.name)
}
