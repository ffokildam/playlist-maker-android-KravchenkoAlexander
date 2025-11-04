package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                SettingsScreen(onBackClick = { finish() })
            }
        }
    }
}

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                SettingItem(
                    title = stringResource(R.string.dark_theme),
                    trailing = {
                        Switch(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF3772E7),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFD9D9D9),
                                uncheckedBorderColor = Color.White
                            )
                        )
                    }
                )

                SettingItem(
                    title = stringResource(R.string.share_app),
                    iconEnd = ImageVector.vectorResource(id = R.drawable.ic_share),
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message))
                        }
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    }
                )

                SettingItem(
                    title = stringResource(R.string.contact_us),
                    iconEnd = ImageVector.vectorResource(id = R.drawable.ic_support),
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email_address)))
                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
                        }
                        context.startActivity(intent)
                    }
                )

                SettingItem(
                    title = stringResource(R.string.user_agreement),
                    iconEnd = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                    onClick = {
                        val url = context.getString(R.string.terms_url)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    iconEnd: ImageVector? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        trailing?.invoke()

        iconEnd?.let {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color(0xFFB0B0B0),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
