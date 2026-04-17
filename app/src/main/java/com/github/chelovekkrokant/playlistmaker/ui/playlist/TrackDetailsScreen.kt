package com.github.chelovekkrokant.playlistmaker.ui.playlist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Playlist
import com.github.chelovekkrokant.playlistmaker.domain.Track
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel,
    navigateBack: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    var isFavorite by remember { mutableStateOf(track.favorite) }
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    TrackDetailsLayout(
        track = track,
        playlists = playlists,
        isFavorite = isFavorite,
        showSheet = showSheet,
        onFavoriteToggle = {
            isFavorite = !isFavorite
            scope.launch { playlistsViewModel.toggleFavorite(track, isFavorite) }
            Toast.makeText(context, "isFavorite = $isFavorite", Toast.LENGTH_SHORT).show()
        },
        onAddToPlaylistClick = { showSheet = true },
        onDismissSheet = { showSheet = false },
        onPlaylistSelected = { playlist ->
            scope.launch {
                playlistsViewModel.insertTrackToPlaylist(track, playlist.id)
                Toast.makeText(context, "песня с id ${track.id} в плейлисте ${playlist.id}", Toast.LENGTH_SHORT).show()
                showSheet = false
            }
        },
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackDetailsLayout(
    track: Track,
    playlists: List<Playlist>,
    isFavorite: Boolean,
    showSheet: Boolean,
    onFavoriteToggle: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onDismissSheet: () -> Unit,
    onPlaylistSelected: (Playlist) -> Unit,
    navigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.7f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navigateBack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back)
            )
            Text(track.trackName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = track.trackName,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(track.trackName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(track.artistName, fontSize = 16.sp, color = Color.Gray)
            Text(track.trackTime, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Избранное",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(40.dp).clickable { onFavoriteToggle() }
                )
                Icon(
                    imageVector = Icons.Default.PlaylistAdd,
                    contentDescription = "Добавить в плейлист",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp).clickable { onAddToPlaylistClick() }
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = rememberModalBottomSheetState()
        ) {
            Text(
                text = "Выберите плейлист",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn {
                items(playlists.size) { index ->
                    PlaylistListItem(playlist = playlists[index]) {
                        onPlaylistSelected(playlists[index])
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}

private val previewTrack = Track(2, "Группа крови", "Кино", "04:43", "", false, 0)
private val previewPlaylists = listOf(
    Playlist(1, "Любимые треки", "", List(3) { Track(it.toLong() + 1, "", "", "", "", false, 1) }),
    Playlist(2, "Рок классика", "", List(5) { Track(it.toLong() + 4, "", "", "", "", false, 2) }),
)

@Preview(name = "TrackDetails", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun TrackDetailsPreview() {
    TrackDetailsLayout(
        track = previewTrack,
        playlists = emptyList(),
        isFavorite = false,
        showSheet = false,
        onFavoriteToggle = {},
        onAddToPlaylistClick = {},
        onDismissSheet = {},
        onPlaylistSelected = {},
        navigateBack = {}
    )
}

@Preview(name = "AddToPlaylistSheet", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun AddToPlaylistSheetPreview() {
    TrackDetailsLayout(
        track = previewTrack,
        playlists = previewPlaylists,
        isFavorite = false,
        showSheet = true,
        onFavoriteToggle = {},
        onAddToPlaylistClick = {},
        onDismissSheet = {},
        onPlaylistSelected = {},
        navigateBack = {}
    )
}
