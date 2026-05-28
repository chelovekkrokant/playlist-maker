package com.github.chelovekkrokant.playlistmaker.ui.playlist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.github.chelovekkrokant.playlistmaker.R

private val ScreenBackground = Color(0xFFFFFFFF)
private val FieldBorderColor = Color(0xFFAEAFB4)
private val PlaceholderColor = Color(0xFFAEAFB4)
private val ButtonColor = Color(0xFFAEAFB4)

@Composable
fun OutlinedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, FieldBorderColor, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = hint, color = PlaceholderColor, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun NewPlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: NewPlaylistViewModel,
    onBack: () -> Unit
) {
    val coverImageUri by viewModel.coverImageUri.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setCoverImageUri(saveImageToInternalStorage(context, it)) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) imagePickerLauncher.launch("image/*")
    }

    NewPlaylistContent(
        modifier = modifier,
        coverImageUri = coverImageUri,
        onCoverClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                imagePickerLauncher.launch("image/*")
            } else {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    imagePickerLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        },
        onSave = { name, description ->
            viewModel.createNewPlaylist(name, description)
            onBack()
        },
        onBack = onBack
    )
}

@Composable
private fun NewPlaylistContent(
    modifier: Modifier = Modifier,
    coverImageUri: String? = null,
    onCoverClick: () -> Unit = {},
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.new_playlist_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { onCoverClick() },
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = coverImageUri?.let { Uri.parse(it) },
                contentDescription = stringResource(R.string.playlist_cover),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                if (painter.state is AsyncImagePainter.State.Success) {
                    SubcomposeAsyncImageContent()
                } else {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = stringResource(R.string.add_cover),
                        tint = PlaceholderColor,
                        modifier = Modifier.size(96.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedInputField(
                value = name,
                onValueChange = { name = it },
                hint = stringResource(R.string.new_playlist_name_hint)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedInputField(
                value = description,
                onValueChange = { description = it },
                hint = stringResource(R.string.new_playlist_description_hint)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ButtonColor, RoundedCornerShape(8.dp))
                    .clickable { if (name.isNotBlank()) onSave(name, description) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.new_playlist_create),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val fileName = "playlist_cover_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    return Uri.fromFile(file).toString()
}

@Preview(name = "NewPlaylistScreen", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun NewPlaylistScreenPreview() {
    NewPlaylistContent(onSave = { _, _ -> }, onBack = {})
}
