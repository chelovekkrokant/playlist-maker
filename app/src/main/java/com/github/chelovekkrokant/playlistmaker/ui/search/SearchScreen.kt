package com.github.chelovekkrokant.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Track

private val SearchBarBackground = Color(0xFFE6E8EB)
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
            .height(61.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick?.invoke() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(13.dp))
        AsyncImage(
            model = track.image.ifEmpty { null },
            contentDescription = null,
            placeholder = painterResource(R.drawable.outline_music_video_24),
            error = painterResource(R.drawable.outline_music_video_24),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Text(
                text = "${track.artistName} • ${track.trackTime}",
                fontSize = 11.sp,
                color = SubtitleColor,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = SubtitleColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Composable
private fun NothingFoundIcon() {
    Image(
        painter = painterResource(R.drawable.ic_nothing_found),
        contentDescription = null,
        modifier = Modifier.size(120.dp)
    )
}

@Composable
private fun NetworkErrorIcon() {
    Image(
        painter = painterResource(R.drawable.ic_no_internet),
        contentDescription = null,
        modifier = Modifier.size(120.dp)
    )
}

@Composable
private fun SearchLayout(
    state: SearchScreenState,
    query: String,
    historyList: List<String>,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSearchAction: () -> Unit,
    onClear: () -> Unit,
    onRetry: () -> Unit,
    onHistoryClick: (String) -> Unit,
    onBack: () -> Unit,
    onTrackClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
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
                    .clickable { onBack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.menu_search),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(SearchBarBackground, RoundedCornerShape(12.dp))
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocusChange(it.isFocused) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchAction() }
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = SubtitleColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_hint),
                                    color = SubtitleColor,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                        if (query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.clear_search),
                                tint = SubtitleColor,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onClear() }
                            )
                        }
                    }
                }
            )

            // История внутри того же контейнера
            if (isFocused && query.isEmpty() && historyList.isNotEmpty()) {
                HorizontalDivider(color = SubtitleColor.copy(alpha = 0.2f))
                historyList.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHistoryClick(item) }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
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

        Spacer(modifier = Modifier.height(8.dp))

        // Контент по состоянию
        when (state) {
            is SearchScreenState.Empty -> Unit

            is SearchScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchScreenState.NothingFound -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NothingFoundIcon()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.search_nothing_found),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            is SearchScreenState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        NetworkErrorIcon()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.search_error_title),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.search_error_subtitle),
                            fontSize = 14.sp,
                            color = SubtitleColor,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = onRetry) {
                            Text(stringResource(R.string.retry_button))
                        }
                    }
                }
            }

            is SearchScreenState.Content -> {
                val tracks = state.tracks
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tracks.size) { index ->
                        TrackListItem(track = tracks[index]) { onTrackClick(index) }
                    }
                }
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
    val state by searchViewModel.searchState.collectAsState()
    var query by remember { mutableStateOf("") }
    var historyList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(query) {
        if (query.isEmpty()) {
            historyList = searchViewModel.getHistoryList().reversed()
            searchViewModel.clearSearch()
        }
        searchViewModel.updateQuery(query)
    }

    LaunchedEffect(state) {
        if (state is SearchScreenState.Content) focusManager.clearFocus()
    }

    SearchLayout(
        state = state,
        query = query,
        historyList = historyList,
        isFocused = isFocused,
        focusRequester = focusRequester,
        modifier = modifier,
        onQueryChange = { query = it },
        onFocusChange = { isFocused = it },
        onSearchAction = {
            searchViewModel.search(query)
            focusManager.clearFocus()
        },
        onClear = {
            query = ""
            focusManager.clearFocus()
            searchViewModel.clearSearch()
        },
        onRetry = { searchViewModel.retry() },
        onHistoryClick = { word ->
            query = word
            searchViewModel.search(word)
        },
        onBack = { onClick(null) },
        onTrackClick = { index -> onClick(index) }
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val fakeFocusRequester = FocusRequester()

@Preview(name = "Empty", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewEmpty() {
    SearchLayout(
        state = SearchScreenState.Empty,
        query = "",
        historyList = emptyList(),
        isFocused = false,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}

@Preview(name = "History", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewHistory() {
    SearchLayout(
        state = SearchScreenState.Empty,
        query = "",
        historyList = listOf("Rammstein", "Queen", "Bruno Mars"),
        isFocused = true,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}

@Preview(name = "Loading", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewLoading() {
    SearchLayout(
        state = SearchScreenState.Loading,
        query = "The Beatles",
        historyList = emptyList(),
        isFocused = false,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}

@Preview(name = "NothingFound", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewNothingFound() {
    SearchLayout(
        state = SearchScreenState.NothingFound,
        query = "xyzxyzxyz",
        historyList = emptyList(),
        isFocused = false,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}

@Preview(name = "Error", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewError() {
    SearchLayout(
        state = SearchScreenState.Error,
        query = "The Beatles",
        historyList = emptyList(),
        isFocused = false,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}

@Preview(name = "Content", widthDp = 360, heightDp = 780, showBackground = true)
@Composable
private fun PreviewContent() {
    val fakeTracks = listOf(
        Track(0, "Yesterday (Remastered 2009)", "The Beatles", "2:55", "", false, 0),
        Track(0, "Here Comes The Sun", "The Beatles", "4:01", "", false, 0),
        Track(0, "Let It Be", "The Beatles", "6:01", "", false, 0),
        Track(0, "Come Together", "The Beatles", "4:09", "", false, 0),
    )
    SearchLayout(
        state = SearchScreenState.Content(fakeTracks),
        query = "The Beatles",
        historyList = emptyList(),
        isFocused = false,
        focusRequester = fakeFocusRequester,
        onQueryChange = {}, onFocusChange = {}, onSearchAction = {},
        onClear = {}, onRetry = {}, onHistoryClick = {}, onBack = {}, onTrackClick = {}
    )
}