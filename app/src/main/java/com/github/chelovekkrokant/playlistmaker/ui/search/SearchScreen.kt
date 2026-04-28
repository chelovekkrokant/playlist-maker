package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Track

private val SubtitleColor = Color(0xFFAEAFB4)
private val ScreenBackground = Color(0xFFFFFFFF)

@Composable
fun TrackListItem(
    track: Track,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = track.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${track.artistName} • ${track.trackTime}",
                fontSize = 11.sp,
                color = SubtitleColor
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = SubtitleColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun HistoryRequests(
    historyList: List<String>,
    onClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = SubtitleColor.copy(alpha = 0.3f))
        historyList.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(item) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = SubtitleColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = item, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier,
    searchViewModel: SearchViewModel,
    onClick: (Int?) -> Unit
) {
    val screenState by searchViewModel.searchState.collectAsState()
    var query by remember { mutableStateOf("") }
    var historyList by remember { mutableStateOf<List<String>>(emptyList()) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(query) {
        if (query.isEmpty()) {
            historyList = searchViewModel.getHistoryList().reversed()
            searchViewModel.clearSearch()
        }
        searchViewModel.updateQuery(query)
    }

    LaunchedEffect(screenState) {
        if (screenState is SearchScreenState.Content) focusManager.clearFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        // Хедер
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClick(null) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.search_icon)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.menu_search),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Строка поиска
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.search_songs_placeholder)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { searchViewModel.search(query) }) {
                Text(stringResource(R.string.search_button))
            }
        }

        // История (когда поле пустое)
        if (query.isEmpty() && historyList.isNotEmpty()) {
            HistoryRequests(
                historyList = historyList,
                onClick = { word ->
                    query = word
                    searchViewModel.search(word)
                }
            )
        }

        // Контент
        when (screenState) {
            is SearchScreenState.Empty -> Unit

            is SearchScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchScreenState.NothingFound -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_songs_found))
                }
            }

            is SearchScreenState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.error))
                        Button(onClick = { searchViewModel.search(query) }) {
                            Text(stringResource(R.string.retry_button))
                        }
                    }
                }
            }

            is SearchScreenState.Content -> {
                val tracks = (screenState as SearchScreenState.Content).tracks
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tracks.size) { index ->
                        TrackListItem(track = tracks[index]) { onClick(index) }
                    }
                }
            }
        }
    }
}
