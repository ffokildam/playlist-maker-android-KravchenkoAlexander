package com.practicum.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.PrimaryAccent
import com.practicum.playlistmaker.ui.theme.PrimaryBackground
import com.practicum.playlistmaker.ui.theme.SecondaryBackground
import com.practicum.playlistmaker.ui.theme.SubText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistMakerTheme {
                PlaylistMakerApp()
            }
        }
    }
}

@Composable
fun PlaylistMakerApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        PlaylistMakerScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun PlaylistMakerScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }

    val history = remember {
        listOf(
            Track("Enter Sandman", "Metallica", "5:31"),
            Track("Chop Suey!", "System Of A Down", "3:29"),
            Track("Back In Black", "AC/DC", "4:14")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(56.dp))
        Text(
            text = "Playlist Maker",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(text = "Поиск трека", color = SubText)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = SubText
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = SubText,
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SecondaryBackground,
                unfocusedContainerColor = SecondaryBackground,
                disabledContainerColor = SecondaryBackground,
                cursorColor = PrimaryAccent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Ранее вы искали",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        history.forEach { track ->
            HistoryItem(track = track)
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { searchQuery = "" },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = PrimaryAccent)
        ) {
            Text(text = "Очистить историю", fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Подборки",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PlaylistCategoryCard(
                title = "Избранные треки",
                description = "25 треков",
                accent = PrimaryAccent
            )
            PlaylistCategoryCard(
                title = "Мои плейлисты",
                description = "Создавай подборки и делись ими",
                accent = Color(0xFF7F8CFF)
            )
            PlaylistCategoryCard(
                title = "Слушай прямо сейчас",
                description = "Подборка лучшего за неделю",
                accent = Color(0xFF5BE6B0)
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)
        ) {
            Text(text = "Создать плейлист", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistMakerPreview() {
    PlaylistMakerTheme {
        PlaylistMakerApp()
    }
}

data class Track(
    val title: String,
    val artist: String,
    val duration: String
)

@Composable
private fun HistoryItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SecondaryBackground)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2C2C2C))
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = track.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = track.artist,
                color = SubText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = track.duration,
            color = SubText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PlaylistCategoryCard(
    title: String,
    description: String,
    accent: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SecondaryBackground,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(accent)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    color = SubText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
