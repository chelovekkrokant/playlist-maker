package com.github.chelovekkrokant.playlistmaker.ui.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chelovekkrokant.playlistmaker.R
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchState
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchViewModel

class AllTracksActivity : ComponentActivity() {
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AllTracksScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "Трек ${track.trackName}",
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${track.artistName} \u2022 ${track.trackTime}",
                fontSize = 11.sp,
                color = Color(0xFFAEAFB4)
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFAEAFB4)
        )
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
            val tracks = (screenState as SearchState.Success).foundList
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(tracks) { track ->
                    TrackListItem(track = track)
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

@Composable
fun TrackScreenContent(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "Трек ${track.trackName}"
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(track.trackName, fontWeight = FontWeight.Bold)
            Text(
                text = "${track.artistName} \u2022 ${track.trackTime}",
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewAllTracksList() {
    val tracks = listOf(
        Track("Владивосток 2000", "Мумий Троль", "2:38"),
        Track("Группа крови", "Кино", "4:43"),
        Track("Не смотри назад", "Ария", "5:12"),
        Track("Звезда по имени Солнце", "Кино", "3:45"),
        Track("Лондон", "Аквариум", "4:32"),
        Track("На заре", "Альянс", "3:50"),
        Track("Перемен", "Кино", "4:56"),
        Track("Розовый фламинго", "Сплин", "3:15"),
        Track("Танцевать", "Мельница", "3:42"),
        Track("Чёрный бумер", "Серега", "4:01"),
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tracks) { track ->
            TrackListItem(track = track)
            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewTrackListItem() {
    val track = Track(
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38"
    )
    TrackListItem(track)
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewTrackScreenContent() {
    val track = Track(
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38"
    )
    TrackScreenContent(track)
}
