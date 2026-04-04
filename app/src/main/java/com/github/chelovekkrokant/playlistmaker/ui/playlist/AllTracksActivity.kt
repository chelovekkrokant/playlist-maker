package com.github.chelovekkrokant.playlistmaker.ui.playlist

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.domain.Track
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchState
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchViewModel

class AllTracksActivity : ComponentActivity() {
    private val viewModel by viewModels<AllTracksViewModel> {
        AllTracksViewModel.getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AllTracksScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "Трек ${track.trackName}"
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(track.trackName, fontWeight = FontWeight.Bold)
            Text(track.artistName)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(track.trackTime)
        }
    }
}

@Composable
fun AllTracksScreen(modifier: Modifier, viewModel: SearchViewModel) {
    val screenState by viewModel.allTracksScreenState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.fetchData()
    }
    when (screenState) {
        is SearchState.Initial -> {}
        is SearchState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SearchState.Success -> {
            val screenState = null
            val tracks = (screenState as SearchState.Success).foundList
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tracks.size) { index ->
                    TrackListItem(track = tracks[index])
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }

        is SearchState.Error -> {
            val error = (screenState as SearchState.Error).error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $error", color = Color.Red)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackListItem() {
    val track = Track(
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38"
    )
    TrackListItem(track)
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackScreenContent() {
    val track = Track(
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38"
    )
    TrackScreenContent(track)
}