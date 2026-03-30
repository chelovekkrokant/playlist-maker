package com.github.chelovekkrokant.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen(onBack = { finish() })
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    val emailAddress = stringResource(R.string.email_address)
    val emailTag = stringResource(R.string.email_tag)
    val emailBody = stringResource(R.string.email_body)
    val shareLink = stringResource(R.string.share_app_link)
    val agreementUrl = stringResource(R.string.user_agreement)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    contentDescription = stringResource(R.string.settings_back),
                )
            }
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 22.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsTabSwitch(message = stringResource(R.string.settings_dark_theme))

        SettingsTab(
            message = stringResource(R.string.settings_share),
            icon = Icons.Default.Share
        ) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareLink)
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
        }

        SettingsTab(
            message = stringResource(R.string.settings_support),
            icon = Icons.Default.SupportAgent
        ) {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                putExtra(Intent.EXTRA_SUBJECT, emailTag)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }
            context.startActivity(supportIntent)
        }

        SettingsTab(
            message = stringResource(R.string.settings_agreement),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight
        ) {
            val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
                data = agreementUrl.toUri()
            }
            context.startActivity(agreementIntent)
        }
    }
}

@Composable
fun SettingsTab(message: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, bottom = 21.dp, top = 21.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFAEAFB4),
            modifier = Modifier
                .padding(end = 18.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun SettingsTabSwitch(message: String) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onBack = {})
}