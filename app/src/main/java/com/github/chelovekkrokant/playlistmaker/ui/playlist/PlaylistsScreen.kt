package com.github.chelovekkrokant.playlistmaker.ui.playlist

import android.net.Uri
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track

@Composable
fun PlaylistListItem(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick.invoke() })
            .height(61.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SubcomposeAsyncImage(
            model = playlist.coverImageUri?.let { Uri.parse(it) },
            contentDescription = playlist.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(48.dp)
        ) {
            if (painter.state is AsyncImagePainter.State.Success) {
                SubcomposeAsyncImageContent()
            } else {
                Icon(
                    imageVector = Icons.Filled.AddPhotoAlternate,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(playlist.name, fontSize = 16.sp)
            Text(stringResource(R.string.tracks_count, playlist.tracks.size), fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PlaylistsScreen(
    modifier: Modifier,
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    PlaylistsLayout(
        modifier = modifier,
        playlists = playlists,
        addNewPlaylist = addNewPlaylist,
        navigateToPlaylist = navigateToPlaylist,
        navigateBack = navigateBack
    )
}

@Composable
private fun PlaylistsLayout(
    modifier: Modifier,
    playlists: List<Playlist>,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navigateBack() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.search_icon)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Плейлисты", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp),
            ) {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(playlists.size) { index ->
                        PlaylistListItem(playlist = playlists[index]) {
                            navigateToPlaylist(playlists[index].id)
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            onClick = { addNewPlaylist() },
            containerColor = Color.Gray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_playlist)
            )
        }
    }
}

private val previewPlaylists = listOf(
    Playlist(id = 1, name = "Любимые треки", description = "", tracks = List(3) { Track(it.toLong() + 1, "", "", "", "", false, 1) }),
    Playlist(id = 2, name = "Рок классика", description = "", tracks = List(5) { Track(it.toLong() + 4, "", "", "", "", false, 2) }),
    Playlist(id = 3, name = "Утренний", description = "", tracks = List(2) { Track(it.toLong() + 9, "", "", "", "", false, 3) }),
)

@Preview(name = "PlaylistsScreen", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PlaylistsScreenPreview() {
    PlaylistsLayout(
        modifier = Modifier,
        playlists = previewPlaylists,
        addNewPlaylist = {},
        navigateToPlaylist = {},
        navigateBack = {}
    )
}
