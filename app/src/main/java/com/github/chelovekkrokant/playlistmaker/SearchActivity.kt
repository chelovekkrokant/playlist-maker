package com.github.chelovekkrokant.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen(onBack = { finish() })
        }
    }
}

@Composable
fun SearchScreen(onBack: () -> Unit) {
    var searchValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchHeader(onBack)
        SearchTextField(
            value = searchValue,
            onValueChange = { searchValue = it },
            onClear = { searchValue = "" }
        )
    }
}


@Composable
fun SearchHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
            )
        }
        Text(
            text = "Поиск",
            fontSize = 22.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(36.dp)
            .background(Color(0xFFE6E8EB), RoundedCornerShape(8.dp)),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFAEAFB4),
                    modifier = Modifier
                        .size(16.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Поиск",
                            color = Color(0xFFAEAFB4),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color(0xFFAEAFB4),
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                }
            }
        }
    )
}


@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(onBack = {})
}
