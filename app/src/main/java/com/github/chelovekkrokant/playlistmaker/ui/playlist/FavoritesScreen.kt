package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.ui.search.TrackListItem

private val ScreenBackground = Color(0xFFFFFFFF)
private val EmptyTextColor = Color(0xFF1A1B22)

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    playlistsViewModel: PlaylistsViewModel,
    navigateBack: () -> Unit,
    navigateToTrack: (Track) -> Unit = {}
) {
    val favorites by playlistsViewModel.favoriteList.collectAsState(emptyList())
    FavoritesContent(
        modifier = modifier,
        favorites = favorites,
        navigateToTrack = { track ->
            playlistsViewModel.selectedTrack = track
            navigateToTrack(track)
        }
    )
}

@Composable
private fun FavoritesContent(
    modifier: Modifier = Modifier,
    favorites: List<Track>,
    navigateToTrack: (Track) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.favorites_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(4.dp)
            )
        }

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_nothing_found),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.favorites_empty),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmptyTextColor
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites.size) { index ->
                    val track = favorites[index]
                    TrackListItem(track = track) { navigateToTrack(track) }
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE6E8EB))
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    FavoritesContent(favorites = emptyList())
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun FavoritesWithTracksPreview() {
    val tracks = listOf(
        Track(1L, "Yesterday (Remastered 2009)", "The Beatles", "2:55", "", true, 0L),
        Track(2L, "Here Comes The Sun (Remastered…", "The Beatles", "2:55", "", true, 0L),
        Track(3L, "No Reply", "The Beatles", "5:41", "", true, 0L),
        Track(4L, "Let It Be", "The Beatles", "3:11", "", true, 0L),
        Track(5L, "Girl", "The Beatles", "6:01", "", true, 0L),
    )
    FavoritesContent(favorites = tracks)
}