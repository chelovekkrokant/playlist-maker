package com.github.chelovekkrokant.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    Column(
        modifier = Modifier.fillMaxSize()
    ){
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color(0xFFE6E8EB), RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier
                    .width(12.dp))
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Назад",
                    modifier = Modifier
                        .size(16.dp),
                    tint = Color(0xFFAEAFB4)
                )

                Spacer(modifier = Modifier
                    .width(8.dp))

                Text(
                    text = "Поиск",
                    fontSize = 16.sp,
                    color = Color(0xFFAEAFB4)
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(onBack = {})
}
