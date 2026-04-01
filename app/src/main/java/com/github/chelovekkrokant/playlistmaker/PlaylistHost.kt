package com.github.chelovekkrokant.playlistmaker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
