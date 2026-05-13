package com.github.chelovekkrokant.playlistmaker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.chelovekkrokant.playlistmaker.ui.main.MainScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.FavoritesScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.NewPlaylistScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistViewModel
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistsScreen
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistsViewModel
import com.github.chelovekkrokant.playlistmaker.ui.playlist.TrackDetailsScreen
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchScreen
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchViewModel
import com.github.chelovekkrokant.playlistmaker.ui.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistHost(navController: NavHostController) {
    val playlistsViewModel: PlaylistsViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.Search.name) },
                onSettingsClick = { navController.navigate(Screen.Settings.name) },
                onPlaylistsClick = { navController.navigate(Screen.Playlist.name) },
                onFavoritesClick = { navController.navigate(Screen.Favorites.name) }
            )
        }
        composable(Screen.Search.name) {
            val searchViewModel: SearchViewModel = koinViewModel()
            SearchScreen(
                modifier = Modifier,
                searchViewModel = searchViewModel,
                onClick = { index ->
                    if (index == null) {
                        navController.popBackStack()
                    } else {
                        val tracks = searchViewModel.getFoundTracks()
                        if (index < tracks.size) {
                            playlistsViewModel.selectedTrack = tracks[index]
                            navController.navigate("${Screen.TrackDetails.name}/$index")
                        }
                    }
                }
            )
        }
        composable(Screen.Settings.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Playlist.name) {
            PlaylistsScreen(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Screen.NewPlaylist.name) },
                navigateToPlaylist = { navController.navigate("${Screen.PlaylistDetail.name}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Favorites.name) {
            FavoritesScreen(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.NewPlaylist.name) {
            NewPlaylistScreen(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Screen.TrackDetails.name}/{trackIndex}",
            arguments = listOf(navArgument("trackIndex") { type = NavType.StringType })
        ) {
            val track = playlistsViewModel.selectedTrack
            if (track != null) {
                TrackDetailsScreen(
                    track = track,
                    playlistsViewModel = playlistsViewModel,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
        composable(
            route = "${Screen.PlaylistDetail.name}/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            val vm: PlaylistViewModel = koinViewModel(parameters = { parametersOf(playlistId) })
            PlaylistScreen(
                modifier = Modifier,
                playlistViewModel = vm,
                navigateToTrack = { track ->
                    playlistsViewModel.selectedTrack = track
                    navController.navigate("${Screen.TrackDetails.name}/${track.id}")
                },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
