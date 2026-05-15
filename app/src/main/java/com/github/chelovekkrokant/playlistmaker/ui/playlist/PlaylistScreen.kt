package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.ui.search.TrackListItem

private val SubtitleColor = Color(0xFFAEAFB4)

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel,
    navigateToTrack: (Track) -> Unit,
    navigateBack: () -> Unit
) {
    val playlist by playlistViewModel.playlist.collectAsState(null)
    PlaylistLayout(
        modifier = modifier,
        playlist = playlist,
        navigateToTrack = navigateToTrack,
        navigateBack = navigateBack
    )
}

@Composable
private fun PlaylistLayout(
    modifier: Modifier = Modifier,
    playlist: Playlist?,
    navigateToTrack: (Track) -> Unit,
    navigateBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navigateBack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }

        if (playlist == null) return

        val tracks = playlist.tracks
        val totalMinutes = tracks.sumOf { track ->
            val parts = track.trackTime.split(":")
            val m = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val s = parts.getOrNull(1)?.toIntOrNull() ?: 0
            m * 60 + s
        } / 60

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = null,
                        tint = SubtitleColor,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = playlist.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (playlist.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = playlist.description,
                            fontSize = 18.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalMinutes минут • ${tracks.size} треков",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(tracks.size) { index ->
                TrackListItem(track = tracks[index]) {
                    navigateToTrack(tracks[index])
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

private val previewPlaylist = Playlist(
    id = 1,
    name = "Best songs 2021",
    description = "2022",
    tracks = listOf(
        Track(1, "Yesterday (Remastered 2009)", "The Beatles", "2:55", "", false, 1),
        Track(2, "Here Comes The Sun (Remastered…", "The Beatles", "2:55", "", false, 1),
        Track(3, "No Reply", "The Beatles", "5:41", "", false, 1),
        Track(4, "Let It Be", "The Beatles", "3:11", "", false, 1),
    )
)

@Preview(name = "PlaylistScreen", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PlaylistScreenPreview() {
    PlaylistLayout(
        playlist = previewPlaylist,
        navigateToTrack = {},
        navigateBack = {}
    )
}
